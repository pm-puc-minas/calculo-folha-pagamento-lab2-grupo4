import { login } from "../../services/api-service.js";

document.getElementById('loginForm').addEventListener('submit', async function(event) {
    event.preventDefault();
    const email = document.getElementById('email').value;
    const senha = document.getElementById('password').value;
    try {
        await login(email, senha);
        window.location.href = '/frontend/dashboard.html';
    } catch (error) {
        alert(error.message);
    }
});