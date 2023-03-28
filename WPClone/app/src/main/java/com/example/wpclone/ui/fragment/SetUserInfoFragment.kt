package com.example.wpclone.ui.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
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
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.wpclone.R
import com.example.wpclone.ui.activity.MainActivity
import com.example.wpclone.databinding.FragmentSetUserInfoBinding
import com.example.wpclone.model.User
import com.example.wpclone.preferanceManager.PreferanceManager
import com.example.wpclone.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*


class SetUserInfoFragment : Fragment() {
    private lateinit var binding : FragmentSetUserInfoBinding
    private lateinit var firestore: FirebaseFirestore
    private var firebaseUser : FirebaseUser? = null
    private lateinit var storage : FirebaseStorage
    private lateinit var dialog: ProgressDialog
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var preferanceManager: PreferanceManager
    private var selectedImage : Uri? = null
    private var encodedImage : String? = null
    private val IMAGE_GALLERY_CODE =111

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetUserInfoBinding.inflate(inflater,container,false)
        init()
        return binding.root
    }

    private fun init(){
        storage = FirebaseStorage.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        preferanceManager = PreferanceManager(requireContext())
        binding.apply {
            nextButton.setOnClickListener {
                dialog = ProgressDialog(requireActivity())
                dialog.setMessage("Updating profile...")
                dialog.setCancelable(false)
                register()
            }

            roundedImageViewProfilePhotoSet.setOnClickListener {
                showBottomSheet()
            }

            firestore.collection(Constants.userCollection)
                    .whereEqualTo(Constants.userId,firebaseUser!!.uid).get()
                    .addOnCompleteListener {
                        if(it.isSuccessful && it.result.documents.size>0){
                            val imageUrl = it.result.documents.get(0).getString(Constants.imageProfile)
                            val userName = it.result.documents.get(0).getString(Constants.userName)
                            if(imageUrl!=null){
                                Glide.with(requireContext())
                                    .load(imageUrl)
                                    .placeholder(R.drawable.person_placeholder)
                                    .into(roundedImageViewProfilePhotoSet)
                            }
                            if(userName!=null){
                                editTextUserNameSet.setText(userName)
                            }
                        }
                    }
        }
    }
    private fun openGallery(){
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent,"select image"),IMAGE_GALLERY_CODE)
    }

    private fun showBottomSheet() {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_pick,null)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
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

    private fun getFileExtention(uri: Uri): String? {
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(requireContext().contentResolver.getType(uri))
    }

    private fun uploadImageToFirebase(imageUri: Uri){
        if(imageUri!=null){
            dialog.setMessage("Uploading...")
            dialog.setCancelable(false)
            dialog.show()
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

                firestore.collection(Constants.userCollection)
                    .whereEqualTo(Constants.userId,firebaseUser!!.uid)
                    .get().addOnCompleteListener {
                        if(it.isSuccessful && it.result.documents.size>0){
                            val currentUser = it.result.documents.get(0)
                            currentUser.reference.update(Constants.imageProfile,dowload_url).addOnSuccessListener {
                                dialog.dismiss()
                                Toast.makeText(requireContext(),"upload successfully",Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener {
                                dialog.dismiss()
                                Toast.makeText(requireContext(),"upload fail",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            }.addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(),"upload fail",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun register() {
        dialog.show()
        firestore = FirebaseFirestore.getInstance()
        if(!binding.editTextUserNameSet.text.toString().isEmpty()){
            firestore.collection(Constants.userCollection)
                .whereEqualTo(Constants.userId,firebaseUser!!.uid)
                .get().addOnCompleteListener {
                    if(it.isSuccessful && it.result.documents.size>0){
                        val ref = it.result.documents.get(0)
                        ref.reference.update(Constants.userName,binding.editTextUserNameSet.text.toString())
                            .addOnSuccessListener {
                                preferanceManager.setString(Constants.userName,binding.editTextUserNameSet.text.toString())
                                if(selectedImage!=null){
                                    preferanceManager.setString(Constants.imageProfile,encodedImage!!)
                                    uploadImageToFirebase(selectedImage!!)
                                }
                                Toast.makeText(requireContext(),"registration is succesfull", Toast.LENGTH_SHORT).show()
                                val intent = Intent(requireActivity(), MainActivity::class.java)
                                startActivity(intent)
                            }
                    }
                    if(it.exception!=null){
                        dialog.dismiss()
                        Toast.makeText(requireContext(),"Something error", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==IMAGE_GALLERY_CODE && data!=null && data.data!=null && resultCode== Activity.RESULT_OK){
            selectedImage = data.data!!
            binding.roundedImageViewProfilePhotoSet.setImageURI(selectedImage)
            val inputStream = context?.contentResolver?.openInputStream(selectedImage!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            encodedImage = Constants.encodedImage(bitmap)
        }
    }


}

