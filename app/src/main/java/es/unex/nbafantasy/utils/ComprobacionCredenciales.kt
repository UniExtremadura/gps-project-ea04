package es.unex.nbafantasy.utils

class ComprobacionCredenciales private constructor() {

    var fallo: Boolean = false
    var msg: String = ""
    var error: CredentialError = CredentialError.PasswordError

    companion object{

        private val TAG = ComprobacionCredenciales::class.java.canonicalName
        private val MINCHARS = 4

        //checks es un array y en cada posicion tiene:
        private val checks = arrayOf(
            //Posicion 0: Credenciales correctas
            ComprobacionCredenciales().apply {
                fallo = false  //No ha habido fallo. VALIDACION CORRECTA
                msg = "Las credenciales son CORRECTAS"
                error = CredentialError.Success
            },
            //Posicion 1: Username incorrecto
            ComprobacionCredenciales().apply {
                fallo = true  //Ha habido fallo. VALIDACION INCORRECTA
                msg = "Nombre usuario incorrecto"
                error = CredentialError.UsernameError
            },
            //Posicion 2: Contrasena incorrecta
            ComprobacionCredenciales().apply {
                fallo = true   //Ha habido fallo. VALIDACION INCORRECTA
                msg = "Contraseña incorrecta"
                error = CredentialError.PasswordError
            },
            //Posicion 3
            ComprobacionCredenciales().apply {
                fallo = true  //Ha habido fallo. VALIDACION INCORRECTA
                msg = "Las contraseñas no coinciden"
                error = CredentialError.PasswordError
            }

        )

        /**
         * Metodo que comprueba si algunos de los campos del inicio de sesion son incorrectos
         * Devuelve:
         *    -true: cuando se ha producido algun error
         *    -false: cuando todos los elementos son correctos
         */
        fun inicioSesion(nombre: String, contrasena: String): ComprobacionCredenciales {
            //Nombre usuario debe tener mas de 4 caracteres
            return if (nombre.isBlank() || nombre.length < MINCHARS) checks[1]// Username incorrecto

            //Contrasena debe tener mas de 4 caracteres
            else if (contrasena.isBlank() || contrasena.length < MINCHARS) checks[2] // Contrasena incorrecta

            else checks[0] //Credenciales correctas
        }

        /**
         * Metodo que comprueba si algunos de los campos del registro de usuario son incorrectos
         * Devuelve:
         *    -true: cuando se ha producido algun error
         *    -false: cuando todos los elementos son correctos
         */
        fun registro(nombre: String, contrasena: String, repassword: String): ComprobacionCredenciales {
            //Nombre usuario debe tener mas de 4 caracteres
            return if (nombre.isBlank() || nombre.length < MINCHARS) checks[1]  // Username incorrecto

            //Contrasena debe tener mas de 4 caracteres
            else if (contrasena.isBlank() || contrasena.length < MINCHARS) checks[2]  // Contrasena incorrecta

            //Si la contrasena y la repeticion de la contrasena es erronea
            else if (contrasena!=repassword) checks[3] //Contrasena y repeticion NO coinciden
            else checks[0] //Credenciales correctas
        }

        fun compararContrasena(contrasena1: String, contrasena2: String): ComprobacionCredenciales{
            return if (contrasena1!=contrasena2) checks[3]
            else checks[0]

        }
    }


    enum class CredentialError {
        PasswordError, UsernameError, Success
    }
}