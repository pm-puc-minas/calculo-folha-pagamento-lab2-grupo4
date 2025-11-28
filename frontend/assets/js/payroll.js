import { findAllFolhas, findFolhasByPeriodo } from '../../services/api-service.js';
import { getPeriodoAtual } from '../../services/utils.js';
import { getIdByToken, getRoleByToken } from '../../services/decode-jwt.js';

var folhas;

async function carregarFolhas() {
    try {
        const role = getRoleByToken();
        const periodo = getPeriodoAtual();

        if(role == 'ADMIN') {
            folhas = await findAllFolhas();
            console.log(`Total de folhas carregadas: ${folhas.length}`);
        } else {
            let id = getIdByToken();
            folhas = await findFolhasByPeriodo(periodo, id);
            console.log(`Folhas do funcionário ${id} carregadas: ${folhas.length}`);
        }

        if(folhas.length === 0) {
            console.warn('Nenhuma folha de pagamento encontrada.');
            return;
        }

        atualizarEstatisticas();
        atualizarTabela();
    } catch (error) {
        console.error('Erro ao carregar folhas de pagamento:', error);
    }
}

function atualizarEstatisticas() {
    let totalBruto = 0;
    let totalDescontos = 0;
    let totalLiquido = 0;
    let contadores = {};

    folhas.forEach(folha => {
        totalBruto += folha.funcionario.financeiro.salarioBruto;
        totalDescontos += folha.totalDescontos;
        totalLiquido += folha.salarioLiquido;

        contadores[folha.funcionario.id] = true;
    });

    const totalFuncionarios = Object.keys(contadores).length;
    const percentualDesconto = (totalDescontos / totalBruto) * 100;

    document.getElementById('valor-bruto').textContent = `R$ ${totalBruto.toFixed(2)}`;
    document.getElementById('valor-descontos').textContent = `R$ ${totalDescontos.toFixed(2)}`;
    document.getElementById('percentual-descontos').textContent = `${percentualDesconto.toFixed(1)}% do total bruto`;
    document.getElementById('valor-liquido').textContent = `R$ ${totalLiquido.toFixed(2)}`;
    document.getElementById('total-funcionarios-stat').textContent = `${totalFuncionarios} funcionários`;

    console.log(`Estatísticas atualizadas - Bruto: R$ ${totalBruto.toFixed(2)}, Descontos: R$ ${totalDescontos.toFixed(2)}, Líquido: R$ ${totalLiquido.toFixed(2)}`);
}


function atualizarTabela() {
    const tbody = document.getElementById('tabela-folhas');
    tbody.innerHTML = '';

    // Agrupa folhas por período
    const folhasPorPeriodo = {};
    
    folhas.forEach(folha => {
        if (!folhasPorPeriodo[folha.periodo]) {
            folhasPorPeriodo[folha.periodo] = {
                periodo: folha.periodo,
                totalBruto: 0,
                totalDescontos: 0,
                totalProventos: 0,
                totalLiquido: 0,
                contadores: {},
                folhas: []
            };
        }
        
        folhasPorPeriodo[folha.periodo].totalBruto += folha.funcionario.financeiro.salarioBruto;
        folhasPorPeriodo[folha.periodo].totalDescontos += folha.totalDescontos;
        folhasPorPeriodo[folha.periodo].totalProventos += folha.totalProventos || 0;
        folhasPorPeriodo[folha.periodo].totalLiquido += folha.salarioLiquido;
        folhasPorPeriodo[folha.periodo].contadores[folha.funcionario.id] = true;
        folhasPorPeriodo[folha.periodo].folhas.push(folha);
    });

    // Converte para array e ordena por período (mais recentes primeiro)
    const periodos = Object.values(folhasPorPeriodo).sort((a, b) => 
        new Date(b.periodo + '-01') - new Date(a.periodo + '-01')
    );

    periodos.forEach(grupo => {
        const row = document.createElement('tr');
        row.classList.add('border-b', 'border-border', 'hover:bg-muted');
        
        // Converte período de YYYY-MM para formato legível
        const [ano, mes] = grupo.periodo.split('-');
        const meses = ['', 'Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 
                       'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'];
        const periodoFormatado = `${meses[parseInt(mes)]} ${ano}`;
        
        const totalFuncionarios = Object.keys(grupo.contadores).length;

        row.innerHTML = `
            <td class="py-3 px-4 text-sm font-medium text-foreground">${periodoFormatado}</td>
            <td class="py-3 px-4 text-sm text-foreground">${totalFuncionarios}</td>
            <td class="py-3 px-4 text-sm text-foreground">R$ ${grupo.totalBruto.toFixed(2)}</td>
            <td class="py-3 px-4 text-sm text-foreground">R$ ${(grupo.totalDescontos || 0).toFixed(2)}</td>
            <td class="py-3 px-4 text-sm text-foreground">R$ ${(grupo.totalProventos || 0).toFixed(2)}</td>
            <td class="py-3 px-4 text-sm font-medium text-foreground">R$ ${grupo.totalLiquido.toFixed(2)}</td>
            <td class="py-3 px-4">
                <span class="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-green-100 text-green-700">Processada</span>
            </td>
        `;
        tbody.appendChild(row);
    });

    console.log(`Tabela atualizada com ${periodos.length} período(s) agrupado(s)`);
}

document.addEventListener('DOMContentLoaded', async function () {
    const role = getRoleByToken();
    console.log('Role do usuário:', role);

    if(role !== 'ADMIN'){
        alert('Você não possui permissão para acessar esta página');
        window.location.href = 'dashboard.html';
        return;
    }

    await carregarFolhas();
});
