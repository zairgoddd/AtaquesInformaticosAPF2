document.addEventListener('DOMContentLoaded', () => {
    // Form switching functionality
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');
    const showRegister = document.getElementById('showRegister');
    const showLogin = document.getElementById('showLogin');
    
    if (showRegister) {
        showRegister.addEventListener('click', (e) => {
            e.preventDefault();
            loginForm.classList.add('hidden');
            registerForm.classList.remove('hidden');
        });
    }
    
    if (showLogin) {
        showLogin.addEventListener('click', (e) => {
            e.preventDefault();
            registerForm.classList.add('hidden');
            loginForm.classList.remove('hidden');
        });
    }
    
    // Password toggle visibility
    const togglePassword = document.getElementById('togglePassword');
    const passwordField = document.getElementById('password');
    
    if (togglePassword && passwordField) {
        togglePassword.addEventListener('click', () => {
            if (passwordField.type === 'password') {
                passwordField.type = 'text';
                togglePassword.classList.remove('fa-eye');
                togglePassword.classList.add('fa-eye-slash');
            } else {
                passwordField.type = 'password';
                togglePassword.classList.remove('fa-eye-slash');
                togglePassword.classList.add('fa-eye');
            }
        });
    }
    
    // Register password toggle visibility
    const toggleRegisterPassword = document.getElementById('toggleRegisterPassword');
    const registerPasswordField = document.getElementById('registerPassword');
    
    if (toggleRegisterPassword && registerPasswordField) {
        toggleRegisterPassword.addEventListener('click', () => {
            if (registerPasswordField.type === 'password') {
                registerPasswordField.type = 'text';
                toggleRegisterPassword.classList.remove('fa-eye');
                toggleRegisterPassword.classList.add('fa-eye-slash');
            } else {
                registerPasswordField.type = 'password';
                toggleRegisterPassword.classList.remove('fa-eye-slash');
                toggleRegisterPassword.classList.add('fa-eye');
            }
        });
    }
    
    // Check if there's an error or success message to show the appropriate form
    const errorMessage = document.querySelector('.error-message');
    const successMessage = document.querySelector('.success-message');
    
    if (errorMessage && errorMessage.closest('#registerForm')) {
        loginForm.classList.add('hidden');
        registerForm.classList.remove('hidden');
    }
    
    if (successMessage) {
        loginForm.classList.add('hidden');
        registerForm.classList.remove('hidden');
    }
});