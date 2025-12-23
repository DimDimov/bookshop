
function showPass() {
    const getPassword = document.getElementById("input_password");

    if (getPassword.type === "password") {
        getPassword.type = "text";
        document.getElementById("img1").src = "images/hidden.png";
    } else {
        getPassword.type = "password"

        document.getElementById("img1").src = "images/eye.png";
    }
}

function validate() {

    const name =
        document.getElementById("input_email").value.trim();
    const password =
        document.getElementById("input_password").value.trim();

    const emailError = document.getElementById(
        "nameErr"
    );
    const passwordError = document.getElementById(
        "passErr"
    );

    emailError.textContent = "";
    passwordError.textContent = "";

    let isValid = true;

    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-z]{2,}$/;

    if(!emailRegex.test(name)) {
        emailError.textContent = window.i18n.invalidEmail
            /*"Bitte geben Sie ein gültiges E-Mail ein."*/
        ;
        isValid = false;
    }

    if (password === "" || password.length < 6) {
        passwordError.textContent = window.i18n.shortPass
            /*"Bitte geben Sie ein Passwort mit mindestens 6 Zeichen ein."*/
        ;
        isValid = false;
    }
    return isValid;
}




