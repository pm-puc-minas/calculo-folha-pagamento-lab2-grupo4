import { findFolhasByPeriodo } from '../../services/api-service.js';
import { getPeriodoAtual, getPeriodoAnterior, totalFuncionarios, getUserName } from '../../services/utils.js';

var folhas;

async function atualizarNomeUsuario() {
    const usernameSpan = document.getElementById('username');
    let nome = await getUserName();
    usernameSpan.textContent = nome;
}

function criarTabela() {
    const tabelaContainer = document.getElementById('folhas-recentes');

    tabelaContainer.innerHTML = '';

    const tabela = document.createElement('table');
    tabela.classList.add('w-full');
    tabela.innerHTML = `
        <thead>
            <tr class="border-b border-border">
                <th class="text-left py-3 px-4 text-sm font-medium text-muted-foreground">Período</th>
              <th class="text-left py-3 px-4 text-sm font-medium text-muted-foreground">Funcionário</th>
              <th class="text-left py-3 px-4 text-sm font-medium text-muted-foreground">Total Bruto</th>
              <th class="text-left py-3 px-4 text-sm font-medium text-muted-foreground">Descontos</th>
              <th class="text-left py-3 px-4 text-sm font-medium text-muted-foreground">Total Líquido</th>
            </tr>
        </thead>
        <tbody id="tabela-folhas-recentes">
        </tbody>
    `;

    tabelaContainer.appendChild(tabela);
}

async function carregarFolhasRecentes(periodo) {
    try {
        folhas = await findFolhasByPeriodo(periodo);

        if(folhas.length === 0) {
            console.log('Nenhuma folha de pagamento encontrada para o período:', periodo);
            carregarFolhasRecentes(getPeriodoAnterior(periodo));
            return 0; // Indica que não há folhas no período atual, mas tentou carregar do período anterior
        }

        criarTabela();

        const tabela = document.getElementById('tabela-folhas-recentes');

        folhas.forEach(folha => {
            console.log(folha);
            const row = document.createElement('tr');
            row.classList.add('border-b', 'border-border', 'hover:bg-muted');
            row.innerHTML = `
                <td class="py-3 px-4 text-sm text-foreground">${folha.periodo}</td>
                <td class="py-3 px-4 text-sm text-foreground">${folha.funcionario.nome}</td>
                <td class="py-3 px-4 text-sm text-foreground">R$ ${folha.funcionario.financeiro.salarioBruto.toFixed(2)}</td>
                <td class="py-3 px-4 text-sm text-foreground">R$ ${folha.totalDescontos.toFixed(2)}</td>
                <td class="py-3 px-4 text-sm text-foreground">R$ ${folha.salarioLiquido.toFixed(2)}</td>
            `;
            tabela.appendChild(row);
        });

        console.log('Folhas de pagamento carregadas com sucesso.');
        return 1; // Indica sucesso ao carregar folhas recentes
    } catch (error) {
        console.error('Erro ao carregar folhas recentes:', error);
        return -1; // Indica erro ao carregar folhas recentes
    }
}

function mediaSalarial() {
    let media = 0;

    folhas.forEach(folha => {
        media += folha.funcionario.financeiro.salarioBruto;
    });

    media = media / folhas.length;
    console.log('Média Salarial Calculada:', media);
    return media;
}

function totalFolhaMes() {
    let total = 0;

    folhas.forEach(folha => {
        total += folha.salarioLiquido;
    });

    console.log('Total da Folha do Mês Calculada:', total);
    return total;
}

function totalDescontos() {
    let total = 0;

    folhas.forEach(folha => {
        total += folha.totalDescontos;
    });

    console.log('Total de Descontos Calculado:', total);
    return total;
}

async function atualizarEstatisticas() {
    document.getElementById('total-funcionarios').textContent = await totalFuncionarios();
    document.getElementById('total-folha-mes').textContent = `R$ ${ totalFolhaMes().toFixed(2) }`;
    document.getElementById('media-salarios').textContent = `R$ ${ mediaSalarial().toFixed(2) }`;
    document.getElementById('total-descontos').textContent = `R$ ${ totalDescontos().toFixed(2) }`;
}

document.addEventListener('DOMContentLoaded', async function () {
    if (!localStorage.getItem('token')) {
      window.location.href = 'login.html';
    }

    await atualizarNomeUsuario();

    let periodo = getPeriodoAtual();

    let folhasRecentes = await carregarFolhasRecentes(periodo);
    if(folhasRecentes === 0) {
        periodo = getPeriodoAnterior(periodo);
        alert('Nenhuma folha de pagamento encontrada para o período atual. Exibindo folhas do período anterior: ' + periodo);
    }

    await atualizarEstatisticas();

});