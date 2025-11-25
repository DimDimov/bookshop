
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
        document.getElementById("input_email").value;
    const password =
        document.getElementById("input_password").value;

    const emailError = document.getElementById(
        "nameErr"
    );
    const passwordError = document.getElementById(
        "passErr"
    );

    emailError.textContent = "";
    passwordError.textContent = "";

    let isValid = true;

    if (password === "" || password.length < 6) {
        passwordError.textContent =
            "Bitte geben Sie ein Passwort mit mindestens 6 Zeichen ein.";
        isValid = false;
    }
    return isValid;
}




