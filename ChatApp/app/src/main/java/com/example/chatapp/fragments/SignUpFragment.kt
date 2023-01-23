package com.example.chatapp.fragments

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Patterns
import androidx.navigation.Navigation
import com.example.chatapp.R
import com.example.chatapp.activities.MainActivity
import com.example.chatapp.databinding.FragmentSignUpBinding
import com.example.chatapp.utilities.Constants
import com.example.chatapp.utilities.PreferenceManager
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException


class SignUpFragment : Fragment() {
    private lateinit var binding : FragmentSignUpBinding
    private var encodedImage :String = ""
    private lateinit var preferenceManager : PreferenceManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSignUpBinding.inflate(inflater,container,false)
        preferenceManager= PreferenceManager(requireContext())
        binding.imageFrame.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            pickImage.launch(intent)
        }

        binding.btnSignUp.setOnClickListener {
            if(isValidSignUp()){
                signUp()
            }
        }

        binding.textViewSignIn.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.fromSignUpToSignIn)
        }

        return binding.root
    }


    private fun encodedImage(bitmap: Bitmap):String{
        val previewWidth = 150
        val previewHeigth = bitmap.height*previewWidth/bitmap.width
        val previewBitmap = Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeigth,false)
        val byteArrayOutputStream = ByteArrayOutputStream()
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream)
        val bytes = byteArrayOutputStream.toByteArray()
        return android.util.Base64.encodeToString(bytes,android.util.Base64.DEFAULT)
    }

    private val pickImage =registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            it->if(it.resultCode== RESULT_OK){
                    if(it.data!=null){
                        val imageUri :Uri= it.data!!.data!!
                        try {
                            val inputStream = context?.contentResolver?.openInputStream(imageUri)
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            binding.imageProfile.setImageBitmap(bitmap)
                            binding.imageText.visibility= View.INVISIBLE
                            encodedImage = encodedImage(bitmap)
                        }catch (e:FileNotFoundException){
                            e.printStackTrace()
                        }
                    }

            }
    }


    private fun signUp(){
        loading(true)
        val database = FirebaseFirestore.getInstance()
        val user = HashMap<String,Any>()
        user.put(Constants.KEY_NAME,binding.editTextNameSignUp.text.toString().trim())
        user.put(Constants.KEY_EMAIL,binding.editTextEmailSignUp.text.toString().trim())
        user.put(Constants.KEY_PASSWORD,binding.editTextPasswordSignUp.text.toString().trim())
        user.put(Constants.KEY_IMAGE,encodedImage)
        database.collection(Constants.KEY_COLLECTION_USERS).add(user)
            .addOnSuccessListener {
                loading(false)
                preferenceManager.putBoolean(Constants.KEY_IS_SIGNED,true)
                preferenceManager.putString(Constants.KEY_USER_ID,it.id)
                preferenceManager.putString(Constants.KEY_NAME,binding.editTextNameSignUp.text.toString().trim())
                preferenceManager.putString(Constants.KEY_IMAGE,encodedImage)
                Navigation.findNavController(requireView()).navigate(R.id.fromSignUpToHome)
            }
            .addOnFailureListener {
                loading(false)
                showToast(it.message.toString())
            }
    }
    private fun isValidSignUp():Boolean{
        if(encodedImage ==""){
            showToast("set up image")
            return false
        }
        else if(binding.editTextNameSignUp.text.toString().trim().isEmpty()){
            showToast("name field can not left empty")
            return false
        }
        else if(binding.editTextEmailSignUp.text.toString().trim().isEmpty()){
            showToast("email field can not left empty")
            return false
        }
//        else if(!Patterns.EMAIL_ADDRESS.matcher(binding.editTextEmailSignUp.text.toString().trim()).matches()){
//            showToast()
//            return false
//        }
        else if(binding.editTextPasswordSignUp.text.toString().trim().isEmpty()){
            showToast("password field can not left empty")
            return false
        }
        else if(binding.editTextConfirmPasswordSignUp.text.toString().isEmpty()){
            showToast("confirm password field can not left empty")
            return false
        }
        else if(!binding.editTextConfirmPasswordSignUp.text.toString().trim()
                .equals(binding.editTextPasswordSignUp.text.toString())){
            showToast("passwords is not same")
            return false
        }
        else{
            return true
        }
    }

    private fun showToast(message : String){
        Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
    }

    private fun loading(isLoading:Boolean){
        if(isLoading){
            binding.btnSignUp.visibility=View.INVISIBLE
            binding.progressBar.visibility=View.VISIBLE
        }else{
            binding.btnSignUp.visibility=View.VISIBLE
            binding.progressBar.visibility=View.INVISIBLE
        }
    }
}