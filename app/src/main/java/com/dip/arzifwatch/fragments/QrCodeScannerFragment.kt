package com.dip.arzifwatch.fragments

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.*
import com.dip.arzifwatch.R
import com.dip.arzifwatch.databinding.FragmentQrCodeScannerBinding
import com.dip.arzifwatch.utils.Utils
import com.permissionx.guolindev.PermissionX

class QrCodeScannerFragment : Fragment(R.layout.fragment_qr_code_scanner) {

    private lateinit var binding: FragmentQrCodeScannerBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentQrCodeScannerBinding.bind(view)

        PermissionX.init(requireActivity())
            .permissions(Manifest.permission.CAMERA)
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    startScanner()
                } else {
                    findNavController().navigateUp()
                }
            }

    }

    private fun startScanner() {
        val codeScanner = CodeScanner(requireContext(), binding.scannerView)

        codeScanner.isAutoFocusEnabled = true
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE

        codeScanner.decodeCallback = DecodeCallback {
            requireActivity().runOnUiThread {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    Utils.QR_KEY,
                    it.text
                )
                findNavController().popBackStack()
            }
        }
        codeScanner.errorCallback = ErrorCallback {
            requireActivity().runOnUiThread {
                Toast.makeText(
                    requireContext(), "Camera initialization error",
                    Toast.LENGTH_LONG
                ).show()
                findNavController().navigateUp()
            }
        }
        codeScanner.startPreview()
    }

}