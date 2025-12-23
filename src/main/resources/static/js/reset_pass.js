
function validateForm() {

    const pass1 =
        document.getElementById("password1").value;
    const pass2 =
        document.getElementById("password2").value;

    const not_match = document.getElementById(
        "not_match");

    const err1 = document.getElementById("passErr1");
    const err2 = document.getElementById("passErr2");

    not_match.textContent = " ";
    err1.textContent = " ";
    err2.textContent = " ";

    let isValid = true;

    if (pass1.length  < 6) {
        err1.textContent =
            window.i18n.shortPass;
        isValid = false;
    }
        if (pass2.length < 6) {
        err2.textContent =
            window.i18n.shortPass;
        isValid = false;
    }

    if (pass1 !== pass2) {
        not_match.textContent =
          window.i18n.noMatch;
        isValid = false;
    }
    return isValid;
}

function showPass2() {
    const getPassword = document.getElementById("password1");

    if (getPassword.type === "password") {
        getPassword.type = "text";
        document.getElementById("img2").src = "images/hidden.png";
    } else {
        getPassword.type = "password"

        document.getElementById("img2").src = "images/eye.png";
    }
}


function showPass3() {
    const getPassword = document.getElementById("password2");

    if (getPassword.type === "password") {
        getPassword.type = "text";
        document.getElementById("img3").src = "images/hidden.png";
    } else {
        getPassword.type = "password"

        document.getElementById("img3").src = "images/eye.png";
    }
}
