import { findAllFuncionarios, findFuncionarioById } from "./api-service.js";
import { getIdByToken } from "./decode-jwt.js";

export async function totalFuncionarios() {
    let funcionarios = await findAllFuncionarios();
    console.log('Total de Funcionários:', funcionarios.length);
    return funcionarios.length;
}

export async function getUserName(){
    let id = getIdByToken();
    let funcionario = await findFuncionarioById(id);
    return funcionario.nome;
}

/**
 * Retorna o período atual no formato AAAA-MM.
 * @returns {string} O período atual.
 */
export function getPeriodoAtual() {
    const date = new Date();
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    
    return `${year}-${month}`;
}

/**
 * Retorna o período anterior ao informado no formato AAAA-MM.
 * Se `periodo` não for informado, usa o período atual.
 * @param {string} periodo - Período no formato 'YYYY-MM'
 * @returns {string} Período anterior no formato 'YYYY-MM'
 */
export function getPeriodoAnterior(periodo) {
    let year, month;
    if (periodo) {
        const parts = periodo.split('-').map(Number);
        year = parts[0];
        month = parts[1];
    } else {
        const date = new Date();
        year = date.getFullYear();
        month = date.getMonth() + 1;
    }

    month -= 1;
    if (month === 0) {
        month = 12;
        year -= 1;
    }

    return `${year}-${String(month).padStart(2, '0')}`;
}