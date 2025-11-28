import { getUserName } from '../../services/utils.js';
import { getRoleByToken } from '../../services/decode-jwt.js';

async function atualizarNomeUsuario() {
    const usernameSpan = document.getElementById('username');
    let nome = await getUserName();
    usernameSpan.textContent = nome;
}

document.addEventListener('DOMContentLoaded', async function () {
    if (!localStorage.getItem('token')) {
      window.location.href = 'login.html';
    }

    let role = getRoleByToken();

    if(role !== 'ADMIN'){
        document.getElementById('payroll-page').style.display = 'none';
    }

    await atualizarNomeUsuario();
});