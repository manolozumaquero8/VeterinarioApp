package com.example.gestionpersonal.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.gestionpersonal.R
import com.example.gestionpersonal.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private var email = ""
    private var pass = ""

    //Lanzador inicio de sesion Google
    private val responseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = data.getResult(ApiException::class.java) //Obtener cuenta
                account?.let {
                    val credential = GoogleAuthProvider.getCredential(it.idToken, null) //Firebase
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                navigateToDashboard() //Si el inicio fue exixstoso nos manda a la pantalla principal
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                }
            } catch (e: ApiException) {
                Log.d("GoogleSignInError", e.message.toString())
            }
        } else {
            Toast.makeText(this, "El usuario canceló", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Inicializar Firebase y Configurar los botones
        auth = Firebase.auth
        setListeners()
    }

    private fun setListeners() {
        binding.btnReset.setOnClickListener { clearFields() }
        binding.btnLogin.setOnClickListener { loginWithEmailPassword() }
        binding.btnRegister.setOnClickListener { registerUser() }
        binding.btnGoogle.setOnClickListener { loginWithGoogle() }
    }

    private fun loginWithGoogle() {
        // Inicio de sesión con Google
        val googleConfig = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_client_id)) //ID Firebase
            .requestEmail()
            .build()
        val googleClient = GoogleSignIn.getClient(this, googleConfig)

        googleClient.signOut() //Cierra la sesion anterior
        responseLauncher.launch(googleClient.signInIntent) //Lanzador de inicio de sesion
    }
    //Email y contraseña
    private fun loginWithEmailPassword() {
        if (!validateFields()) return
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (it.isSuccessful) navigateToDashboard()
            }
            .addOnFailureListener { Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show() }
    }
    //Registro usuario
    private fun registerUser() {
        if (!validateFields()) return
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { if (it.isSuccessful) loginWithEmailPassword() } //Inicia sesion automaticamente
            .addOnFailureListener { Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show() }
    }

    private fun validateFields(): Boolean {
        email = binding.etEmail.text.toString().trim() //Obtiene email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Correo no válido"
            return false
        }
        pass = binding.etPass.text.toString().trim() //Obtener contraseña
        if (pass.length < 6) {
            binding.etPass.error = "La contraseña debe tener al menos 6 caracteres"
            return false
        }
        return true
    }
    //Limpia los campos que hemos puesto
    private fun clearFields() {
        binding.etPass.setText("")
        binding.etEmail.setText("")
    }
    //Inicia la pantalla principal y finaliza la actual
    private fun navigateToDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
    //Verifica al inciar si el usuario esta logueado
    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) navigateToDashboard() //Si esta logueado nos manda a la pantalla principal
    }
}
