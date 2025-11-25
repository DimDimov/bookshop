
function showPass1() {
    const getPassword = document.getElementById("regpass");

    if (getPassword.type === "password") {
        getPassword.type = "text";
        document.getElementById("img2").src = "images/hidden.png";
    } else {
        getPassword.type = "password"

        document.getElementById("img2").src = "images/eye.png";
    }
}


/*function showUsername() {
    const checkBox = document.getElementById("use_email");
    const usernameField = document.getElementById("usernameField");
    const wrapper = document.getElementById("myWrapper");



    if (checkBox.checked){
       usernameField.disabled = true;
      wrapper.style.display = 'none';
    } else {
        usernameField.disabled = false;
        wrapper.style.display = 'block';
    }

}
window.onload = function () { showUsername();

    const checkBox = document.getElementById("use_email");
  checkBox.addEventListener("change", showUsername)
}*/

/*function toggleUsernameField() {

    const usernameField = document.getElementById("usernameField");
    const checkBox = document.getElementById("use_email");

    usernameField.disabled = checkBox.checked;
}

checkBox.addEventListener('change',
    toggleUsernameField);
toggleUsernameField();*/

