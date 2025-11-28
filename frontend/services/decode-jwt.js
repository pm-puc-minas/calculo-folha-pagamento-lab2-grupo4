import { jwtDecode } from 'https://cdn.jsdelivr.net/npm/jwt-decode@4.0.0/+esm'

export function getIdByToken() {
    const token = localStorage.getItem("token");
    if (!token) {
        console.warn('Token não encontrado no localStorage');
        return null;
    }
    try {
        const payload = jwtDecode(token);
        return payload?.id || null;
    } catch (error) {
        console.error('Erro ao decodificar JWT:', error);
        return null;
    }
}

export function getRoleByToken() {
    const token = localStorage.getItem("token");
    if (!token) {
        console.warn('Token não encontrado no localStorage');
        return null;
    }
    try {
        const payload = jwtDecode(token);
        return payload?.role || null;
    } catch (error) {
        console.error('Erro ao decodificar JWT:', error);
        return null;
    }
}