package com.example.wpclone.ui.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.wpclone.R
import com.example.wpclone.databinding.FragmentOTPBinding
import com.example.wpclone.model.User
import com.example.wpclone.preferanceManager.PreferanceManager
import com.example.wpclone.utils.Constants
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

class OTPFragment : Fragment() {
    private lateinit var binding : FragmentOTPBinding
    private lateinit var phoneNumber: String
    private lateinit var preferanceManager :PreferanceManager
    private lateinit var mVerificationId :String
    private lateinit var mResendToken : PhoneAuthProvider.ForceResendingToken
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var progresDialog : ProgressDialog
    private lateinit var mCallBack : PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var timer: CountDownTimer
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOTPBinding.inflate(inflater,container,false)
        init()


        return binding.root
    }

    private fun init(){
        preferanceManager = PreferanceManager(requireContext())
        mAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        progresDialog = ProgressDialog(requireContext())

        val bundle : OTPFragmentArgs by navArgs()
        phoneNumber = bundle.phoneNumber

        mCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Log.e("Tag","Verification Completed")
                progresDialog.dismiss()

            }
            override fun onVerificationFailed(p0: FirebaseException) {

            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                mVerificationId = p0
                mResendToken = p1
                progresDialog.dismiss()
                timer = object:CountDownTimer(60000,1000){
                    override fun onTick(millisUntilFinished: Long) {
                        binding.timerTextView.text = "00:${millisUntilFinished/1000}"
                    }
                    override fun onFinish() {
                        binding.msgImageViewOtp.setImageResource(R.drawable.chat_ic_black)
                        binding.resendSMSTextView.setTextColor(resources.getColor(R.color.reverse_text))
                        binding.timerTextView.setTextColor(resources.getColor(R.color.reverse_text))
                    }
                }
                timer.start()
            }
        }

        binding.apply {
            phoneNumberTextViewOTP.text = "Verify "+phoneNumber
            phoneNumberTextViewOTP2.text = phoneNumber

            wrongNumberTextView.setOnClickListener {
                requireActivity().onBackPressed()
            }

            resendSMSTextView.setTextColor(resources.getColor(R.color.gray))
            msgImageViewOtp.setImageResource(R.drawable.chat_ic_gray)
            timerTextView.setTextColor(resources.getColor(R.color.gray))

            resendSMSTextView.setOnClickListener {
                if(timerTextView.text.toString().equals("00:00")){
                    startPhoneNumberVerification(phoneNumber)
                }
            }
        }
        progresDialog.setMessage("Code sending...")
        progresDialog.show()
        startPhoneNumberVerification(phoneNumber)



        binding.pinView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().length==6){
                    progresDialog.setMessage("Verifiying...")
                   verifyPhoneNumberWithCode(mVerificationId,s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun startPhoneNumberVerification(phoneNumber:String){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            requireActivity(),
            mCallBack
        )
    }

    private fun verifyPhoneNumberWithCode(verficationId:String,code:String){
        val credential = PhoneAuthProvider.getCredential(verficationId,code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential){
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    progresDialog.dismiss()
                    Log.e("TAG","succes credential")
                    deleteBackStack()
                    timer.cancel()

                    firestore.collection(Constants.userCollection)
                        .whereEqualTo(Constants.userId,mAuth.currentUser!!.uid)
                        .get().addOnCompleteListener {
                            if(it.isSuccessful && it.result.documents.size>0){

                            }else{
                                val user = User(mAuth.currentUser!!.uid
                                    ,""
                                    ,mAuth.currentUser!!.phoneNumber!!
                                    ,""
                                    ,""
                                    ,""
                                    ,""
                                    ,""
                                    ,"")

                                firestore.collection(Constants.userCollection)
                                    .add(user).addOnSuccessListener {
                                        preferanceManager.setString(Constants.userId,user.userId)
                                        preferanceManager.setString(Constants.userPhone,user.userPhone)
                                        preferanceManager.setBoolean(Constants.isSignedIn,true)
                                        Navigation.findNavController(requireView()).navigate(R.id.fromOTPToSetUserInfo)
                                    }.addOnFailureListener {
                                        Toast.makeText(requireContext(),"fail",Toast.LENGTH_SHORT).show()
                                    }
                            }
                            if(it.exception!=null){
                                progresDialog.dismiss()
                                Log.e("TAG","error sign in")
                            }
                        }
                }
            }
    }

    private fun deleteBackStack(){
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.apply {
            for(i in 0 until  fragmentManager.backStackEntryCount){
                fragmentManager.popBackStack()
            }
        }
    }
}