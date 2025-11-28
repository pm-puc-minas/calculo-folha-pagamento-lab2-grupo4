import { findFolhasByFuncionarioId, findFolhasByPeriodo } from '../../services/api-service.js';
import { getPeriodoAtual, getPeriodoAnterior, totalFuncionarios, getUserName } from '../../services/utils.js';
import { getIdByToken, getRoleByToken } from '../../services/decode-jwt.js';

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

async function carregarFolhasRecentes(role, periodo = null, tentativa = 0) {
    const MAX_TENTATIVAS = 3; // Limite de recursão para evitar loop infinito

    try {
        if(role == 'ADMIN') {
            // Se não houver período, usa o atual
            if(!periodo) {
                periodo = getPeriodoAtual();
            }

            folhas = await findFolhasByPeriodo(periodo);

            // Se não encontrar folhas e ainda não atingiu limite, tenta período anterior
            if(folhas.length === 0 && tentativa < MAX_TENTATIVAS) {
                console.log(`Nenhuma folha encontrada para o período ${periodo}. Tentando período anterior... (tentativa ${tentativa + 1}/${MAX_TENTATIVAS})`);
                const periodoAnterior = getPeriodoAnterior(periodo);
                return await carregarFolhasRecentes(role, periodoAnterior, tentativa + 1);
            }

            if(folhas.length === 0) {
                console.warn(`Nenhuma folha de pagamento encontrada após ${MAX_TENTATIVAS} tentativas.`);
                return -1;
            }
        }
        else{
            let id = getIdByToken();
            folhas = await findFolhasByFuncionarioId(id);

            if(folhas.length === 0) {
                console.log('Nenhuma folha de pagamento encontrada para o funcionário:', id);
                return -1;
            }
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

        console.log(`Folhas de pagamento carregadas com sucesso para o período: ${periodo}`);
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

async function atualizarEstatisticas(role) {
    if(role == 'ADMIN'){
        console.log('Atualizando estatísticas para ADMIN');

        document.getElementById('card-1').textContent = await totalFuncionarios();
        document.getElementById('c1-text').textContent = 'Total de Funcionários';

        document.getElementById('card-2').textContent = `R$ ${ totalFolhaMes().toFixed(2) }`;
        document.getElementById('c2-text').textContent = 'Folha do Mês Atual';

        document.getElementById('card-3').textContent = `R$ ${ mediaSalarial().toFixed(2) }`;
        document.getElementById('c3-text').textContent = 'Média Salarial';

        document.getElementById('card-4').textContent = `R$ ${ totalDescontos().toFixed(2) }`;
        document.getElementById('c4-text').textContent = 'Descontos Totais';
    }
    else{
        const ultimaFolha = folhas.length > 0 ? folhas[folhas.length - 1] : null;
        
        if (ultimaFolha) {
            console.log('Atualizando estatísticas para USER com a última folha:', ultimaFolha);
            
            document.getElementById('card-1').textContent = `R$ ${ ultimaFolha.funcionario.financeiro.salarioBruto.toFixed(2) }`;
            document.getElementById('c1-text').textContent = 'Salário Bruto';

            document.getElementById('card-2').textContent = `R$ ${ ultimaFolha.salarioLiquido.toFixed(2) }`;
            document.getElementById('c2-text').textContent = 'Salário Líquido';
            
            document.getElementById('card-3').textContent = `R$ ${ ultimaFolha.totalProventos.toFixed(2) }`;
            document.getElementById('c3-text').textContent = 'Total de Proventos';

            document.getElementById('card-4').textContent = `R$ ${ ultimaFolha.totalDescontos.toFixed(2) }`;
            document.getElementById('c4-text').textContent = 'Total de Descontos';
        }
    }
}

document.addEventListener('DOMContentLoaded', async function () {
    if (!localStorage.getItem('token')) {
      window.location.href = 'login.html';
    }

    const role = getRoleByToken();
    console.log('Role do usuário:', role);

    if(role == 'USER'){
        document.getElementById('acoes-rapidas').style.display = 'none';
    }

    await atualizarNomeUsuario();

    let folhasRecentes = await carregarFolhasRecentes(role);

    await atualizarEstatisticas(role);

});