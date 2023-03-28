package com.example.wpclone.ui.fragment

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.graphics.drawable.toBitmap
import androidx.core.widget.addTextChangedListener
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.wpclone.R
import com.example.wpclone.databinding.FragmentEditProfileBinding
import com.example.wpclone.preferanceManager.PreferanceManager
import com.example.wpclone.ui.activity.SplashActivity
import com.example.wpclone.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.Objects

class EditProfileFragment : Fragment() {
    private lateinit var binding : FragmentEditProfileBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    private lateinit var preferanceManager: PreferanceManager
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bottomSheetEditNameDialog : BottomSheetDialog
    private var firebaseUser: FirebaseUser? = null
    private lateinit var progressDialog :ProgressDialog
    private lateinit var imageUri :Uri
    private val IMAGE_GALLERY_CODE = 111
    private var encodedImage : String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater,container,false)
        init()
        return binding.root
    }

    private fun init(){
        mAuth = FirebaseAuth.getInstance()
        preferanceManager = PreferanceManager(requireContext())
        firestore = FirebaseFirestore.getInstance()
        firebaseUser = mAuth.currentUser
        progressDialog = ProgressDialog(requireContext())
        onClick()
        getUserInfoOffline()
        getUserInfoOnline()

    }

    private fun onClick(){
        binding.apply {
            backArrowImageView.setOnClickListener {
                requireActivity().onBackPressed()
            }

            cameraImageView.setOnClickListener {
                showBottomSheet()
            }

            editUsernameImageView.setOnClickListener {
                showBottomSheetEditname()
            }
            profilePhotoImageView.setOnClickListener {
                if(preferanceManager.getString(Constants.imageProfile)!=null){
                    it.invalidate()
                    val dr = profilePhotoImageView.drawable
                    Constants.IMAGE_BITMAP = dr.current.toBitmap()
                }
                Navigation.findNavController(it).navigate(R.id.fromEditToViewImage)
            }
            logOutButton.setOnClickListener {
                logOut()
            }
        }
    }

    private fun logOut(){
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("Do you want to sign out?")
        alertDialogBuilder.setPositiveButton("Sign out",object:DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog!!.cancel()
                mAuth.signOut()
                deleteBackStack()
                preferanceManager = PreferanceManager(requireContext())
                preferanceManager.clear()
                startActivity(Intent(requireActivity(),SplashActivity::class.java))
            }
        })
        alertDialogBuilder.setNegativeButton("No",object :DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog!!.cancel()
            }
        })
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

    }

    private fun deleteBackStack(){
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransient = fragmentManager.beginTransaction()
        fragmentTransient.apply {
            for(i in 0 until fragmentManager.backStackEntryCount){
                fragmentManager.popBackStack()
            }
        }
    }

    private fun editTextListener(editText: EditText,button: AppCompatButton){
        editText.addTextChangedListener {
            if(it!!.length==0){
                button.setTextColor(resources.getColor(R.color.gray))
            }else{
                button.setTextColor(resources.getColor(R.color.green))
            }
        }
    }

    private fun showBottomSheetEditname(){
        bottomSheetEditNameDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_editname,null)
        bottomSheetEditNameDialog.setContentView(view)
        bottomSheetEditNameDialog.show()

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Objects.requireNonNull(bottomSheetEditNameDialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS))
        }
        val buttonSave = view.findViewById<AppCompatButton>(R.id.saveButton)
        val buttonCancel = view.findViewById<AppCompatButton>(R.id.cencelButton)
        val editText = view.findViewById<EditText>(R.id.editTextUsername)
        editTextListener(editText,buttonSave)
        buttonSave.setOnClickListener {
            if(editText.text.length>0){
                updateUsername(editText.text.toString())
            }
        }

        buttonCancel.setOnClickListener {
            bottomSheetEditNameDialog.dismiss()
        }
    }


    private fun updateUsername(userName:String){
        progressDialog.setMessage("updating username")
        progressDialog.setCancelable(false)
        progressDialog.show()
        firestore.collection(Constants.userCollection)
            .get().addOnCompleteListener {
                if(it.isSuccessful && it.result.documents.size>0){
                    for(doc in it.result){
                        if(firebaseUser!=null){
                            if(doc.getString(Constants.userId)!!.equals(firebaseUser!!.uid)){
                                doc.reference.update(Constants.userName,userName).addOnSuccessListener {
                                    getUserInfoOnline()
                                    progressDialog.dismiss()
                                    Toast.makeText(requireContext(), "username updated", Toast.LENGTH_SHORT)
                                        .show()
                                    preferanceManager.setString(Constants.userName,userName)
                                    bottomSheetEditNameDialog.dismiss()
                                }.addOnFailureListener {
                                    progressDialog.dismiss()
                                    Toast.makeText(requireContext(),"unable",Toast.LENGTH_SHORT).show()
                                    bottomSheetEditNameDialog.dismiss()
                                }
                            }
                        }

                    }
                }
            }
    }

    private fun showBottomSheet() {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_pick,null)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Objects.requireNonNull(bottomSheetDialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS))
        }

        view.findViewById<LinearLayout>(R.id.galleryLn).setOnClickListener {
            bottomSheetDialog.dismiss()
            openGallery()
        }

        view.findViewById<LinearLayout>(R.id.cameraLn).setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setOnDismissListener {
            it.dismiss()
        }
    }

    private fun openGallery(){
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent,"select image"),IMAGE_GALLERY_CODE)
    }

    private fun getUserInfoOffline(){
        binding.apply {
            userNameTextViewProfileEdit.text = preferanceManager.getString(Constants.userName)
            phoneTextViewProfileEdit.text = preferanceManager.getString(Constants.userPhone)
            if(preferanceManager.getString(Constants.imageProfile)!=null){
                profilePhotoImageView.setImageBitmap(Constants.convertStringToBitmap(preferanceManager.getString(Constants.imageProfile)!!))
            }
            if(preferanceManager.getString(Constants.bio)!=null){
                bioTextViewProfileEdit.text = preferanceManager.getString(Constants.bio)
            }
        }
    }

    private fun getUserInfoOnline(){
        if(firebaseUser!=null){
            firestore.collection(Constants.userCollection).whereEqualTo(Constants.userId,firebaseUser!!.uid)
                .get().addOnCompleteListener {
                    if(it.isSuccessful  && it.result.documents.size>0){
                        val currentUser = it.result.documents.get(0)
                        binding.userNameTextViewProfileEdit.text = currentUser.getString(Constants.userName)
                        binding.phoneTextViewProfileEdit.text = firebaseUser!!.phoneNumber
                        if(currentUser.getString(Constants.bio)!=null && !currentUser.getString(Constants.bio)!!.isEmpty()){
                            binding.bioTextViewProfileEdit.text = currentUser.getString(Constants.bio)
                        }
                        if(!currentUser.getString(Constants.imageProfile)!!.isEmpty()){
                            Glide.with(requireContext())
                                .load(currentUser.getString(Constants.imageProfile))
                                .into(binding.profilePhotoImageView)
                        }
                    }
                }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==IMAGE_GALLERY_CODE && data!=null && data.data!=null && resultCode==RESULT_OK){
            imageUri = data.data!!
            uploadImageToFirebase(imageUri)
            val inputStream = context?.contentResolver?.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            encodedImage = Constants.encodedImage(bitmap)
        }
    }

    private fun getFileExtention(uri: Uri): String? {
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(requireContext().contentResolver.getType(uri))
    }

    private fun uploadImageToFirebase(imageUri: Uri){
        if(imageUri!=null){
            progressDialog.setMessage("Uploading...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            val storageRef = FirebaseStorage.getInstance()
                .reference
                .child("ImagesProfile/"+System.currentTimeMillis()+"."+getFileExtention(imageUri))
            storageRef.putFile(imageUri).addOnSuccessListener {
                val urlTask = it.storage.downloadUrl
                while(!urlTask.isSuccessful){
                    continue
                }
                val dowloadUrl = urlTask.result
                val dowload_url = dowloadUrl.toString()

                if(firebaseUser!=null){
                    firestore.collection(Constants.userCollection)
                        .whereEqualTo(Constants.userId,firebaseUser!!.uid)
                        .get().addOnCompleteListener {
                            if(it.isSuccessful && it.result.documents.size>0){
                                val currentUser = it.result.documents.get(0)
                                currentUser.reference.update(Constants.imageProfile,dowload_url).addOnSuccessListener {
                                    progressDialog.dismiss()
                                    Toast.makeText(requireContext(),"upload successfully",Toast.LENGTH_SHORT).show()
                                    preferanceManager.setString(Constants.imageProfile,encodedImage!!)
                                    getUserInfoOnline()
                                }.addOnFailureListener {
                                    progressDialog.dismiss()
                                    Toast.makeText(requireContext(),"upload fail",Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                }
            }.addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(requireContext(),"upload fail",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getUserInfoOffline()
        getUserInfoOnline()
    }

}