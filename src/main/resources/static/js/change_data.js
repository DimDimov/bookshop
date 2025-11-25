function showPass() {
    const getPassword = document.getElementById('pass');
    const eyeImg = document.getElementById('imgEye')

    if (getPassword.type === 'password') {
        getPassword.type = 'text';
        eyeImg.src = '/images/hidden1.png';
    } else {
        getPassword.type = 'password';

        eyeImg.src = '/images/eye1.png';
    }
}