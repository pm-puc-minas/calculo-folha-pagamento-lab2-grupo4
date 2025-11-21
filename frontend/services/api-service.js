const URL_BASE = 'http://localhost:8080';

function getAuthToken() {
    return localStorage.getItem('token');
}

/**
 * Função de utilidade para todas as chamadas autenticadas
 */
async function apiFetch(endpoint, method = 'GET', body = null) {
    const token = getAuthToken();
    const headers = {
        'Content-Type': 'application/json',
        ...(token && {'Authorization': `Bearer ${token}`}) 
    };

    const config = {
        method: method,
        headers: headers,
        ...(body && {body: JSON.stringify(body)})
    };

    const response = await fetch(`${URL_BASE}${endpoint}`, config);

    if (!response.ok) {
        const errorBody = await response.json().catch(() => ({ message: response.statusText }));
        throw new Error(`Erro na API (${response.status}): ${errorBody.message}`);
    }

    if (response.status === 204) {
        return null;
    }

    return response.json();
}

// CREATE - Autenticação e Cadastro

/**
 * Endpoint: POST /auth/login
 * Realiza a autenticação.
 */
export async function login(email, senha) {
    const response = await fetch(`${URL_BASE}/auth/login`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({ login: email, senha: senha })
    });
    
    if (response.ok) {
        const data = await response.json();
        localStorage.setItem('token', data.token);
        return data;
    }
    
    throw new Error('Falha na autenticação. Verifique suas credenciais.');
}

/**
 * Endpoint: POST /funcionarios
 * Cadastro de funcionário/usuário.
 */

export async function cadastrarFuncionario(funcionario) {
    return apiFetch('/funcionarios', 'POST', funcionario);
}

// CREATE - Geral

/**
 * Endpoint: POST /funcionarios/{id}/folhas/{periodo}
 * Gera uma nova folha de pagamento para um funcionário em um período específico.
 */
export async function gerarFolhaPagamento(id, periodo) {
    return apiFetch(`/funcionarios/${id}/folhas/${periodo}`, 'POST');
}

// READ - Funcionario

/**
 * Endpoint: GET /funcionarios
 * Lista todos os funcionários.
 */
export async function findAllFuncionarios() {
    return apiFetch('/funcionarios');
}

/**
 * Endpoint: GET /funcionarios/{id}
 * Busca um funcionário pelo ID.
 */
export async function findFuncionarioById(id) {
    return apiFetch(`/funcionarios/${id}`);
}

/**
 * Endpoint: GET /funcionarios/{id}/financeiro
 * Busca os dados financeiros de um funcionário pelo ID.
 */
export async function findDadosFinanceirosByFuncionarioId(id) {
    return apiFetch(`/funcionarios/${id}/financeiro`);
}

/**
 * Endpoint: GET /funcionarios/{id}/folhas
 * Busca as folhas de pagamento de um funcionário pelo ID.
 */
export async function findFolhasByFuncionarioId(id) {
    return apiFetch(`/funcionarios/${id}/folhas`);
}

/**
 * Endpoint: GET /funcionarios/{id}/folhas/{periodo}
 * Busca a folha de pagamento de um funcionário por ID e período.
 */
export async function findFolhaByFuncionarioIdAndPeriodo(id, periodo) {
    return apiFetch(`/funcionarios/${id}/folhas/${periodo}`);
}

// READ - Folha de Pagamento

/**
 * Endpoint: GET /folhas
 * Lista todas as folhas de pagamento.
 */
export async function findAllFolhas() {
    return apiFetch('/folhas');
}

/**
 * Endpoint: GET /folhas/{id}
 * Busca uma folha de pagamento pelo ID.
 */
export async function findFolhaById(id) {
    return apiFetch(`/folhas/${id}`);
}

/**
 * Endpoint: GET /folhas/periodo/{periodo}
 * Busca folhas de pagamento por período.
 */
export async function findFolhasByPeriodo(periodo) {
    return apiFetch(`/folhas/periodo/${periodo}`);
}

// DELETE

/**
 * Endpoint: DELETE /funcionarios/{id}/folhas/{periodo}
 * Deleta a folha de pagamento de um funcionário por ID e período.
 */
export async function deleteFolhaByFuncionarioIdAndPeriodo(id, periodo) {
    return apiFetch(`/funcionarios/${id}/folhas/${periodo}`, 'DELETE');
}