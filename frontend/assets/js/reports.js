import { findAllFolhas, findFolhasByFuncionarioId } from '../../services/api-service.js';
import { getRoleByToken, getIdByToken } from '../../services/decode-jwt.js';

var folhas;

async function carregarFolhas() {
    try {
        const role = getRoleByToken();

        if(role == 'ADMIN') {
            folhas = await findAllFolhas();
            console.log(`Total de folhas carregadas: ${folhas.length}`);
            atualizarEstatisticas();
            atualizarGraficoEvolucao();
            atualizarDistribuicaoDepartamento();
        } else {
            const idFuncionario = getIdByToken();
            folhas = await findFolhasByFuncionarioId(idFuncionario);
            console.log(`Total de folhas do usuário carregadas: ${folhas.length}`);
        }

        if(folhas.length === 0) {
            console.warn('Nenhuma folha de pagamento encontrada.');
            return;
        }

        atualizarTabelaRelatorio();

    } catch (error) {
        console.error('Erro ao carregar folhas de pagamento:', error);
    }
}

function atualizarEstatisticas() {
    let totalAnoBruto = 0;
    let totalAnoInss = 0;
    let totalAnoIrrf = 0;
    let totalAnoProventos = 0;

    folhas.forEach(folha => {
        totalAnoBruto += folha.funcionario.financeiro.salarioBruto;
        totalAnoInss += folha.valorINSS || 0;
        totalAnoIrrf += folha.valorIRRF || 0;
        totalAnoProventos += folha.totalProventos || 0;
    });

    const statBruto = document.getElementById('stat-bruto');
    const statInss = document.getElementById('stat-inss');
    const statIrrf = document.getElementById('stat-irrf');
    const statBeneficios = document.getElementById('stat-beneficios');

    if(statBruto) statBruto.textContent = `R$ ${(totalAnoBruto / 1000).toFixed(1)}K`;
    if(statInss) statInss.textContent = `R$ ${(totalAnoInss / 1000).toFixed(0)}K`;
    if(statIrrf) statIrrf.textContent = `R$ ${(totalAnoIrrf / 1000).toFixed(0)}K`;
    if(statBeneficios) statBeneficios.textContent = `R$ ${(totalAnoProventos / 1000).toFixed(1)}K`;
}

function atualizarGraficoEvolucao() {
    const folhasPorPeriodo = {};

    // Agrupa folhas por período (YYYY-MM)
    folhas.forEach(folha => {
        if (!folhasPorPeriodo[folha.periodo]) {
            folhasPorPeriodo[folha.periodo] = 0;
        }
        folhasPorPeriodo[folha.periodo] += folha.funcionario.financeiro.salarioBruto;
    });

    const periodos = Object.keys(folhasPorPeriodo).sort();
    const totais = periodos.map(p => folhasPorPeriodo[p]);

    const maxTotal = Math.max(...totais);

    const graficos = document.getElementById('grafico-evolucao');
    graficos.innerHTML = '';

    periodos.forEach(periodo => {
        const altura = (folhasPorPeriodo[periodo] / maxTotal) * 100;
        const bar = document.createElement('div');
        bar.className = 'flex-1 bg-primary/20 rounded-t';
        bar.style.height = altura + '%';
        bar.innerHTML = `<div class="h-full bg-primary rounded-t"></div>`;
        graficos.appendChild(bar);
    });

    const labels = document.getElementById('labels-evolucao');
    labels.innerHTML = '';
    periodos.forEach(periodo => {
        const label = document.createElement('span');
        label.textContent = periodo.substring(5);
        label.className = 'text-xs text-muted-foreground';
        labels.appendChild(label);
    });
}

function atualizarDistribuicaoDepartamento() {
    const departamentos = {};

    folhas.forEach(folha => {
        const cargo = folha.funcionario.cargo;
        if (!departamentos[cargo]) {
            departamentos[cargo] = 0;
        }
        departamentos[cargo] += folha.funcionario.financeiro.salarioBruto;
    });

    const totalGeral = Object.values(departamentos).reduce((a, b) => a + b, 0);

    const cores = ['bg-primary', 'bg-accent', 'bg-purple-500', 'bg-orange-500', 'bg-pink-500', 'bg-green-500', 'bg-blue-500', 'bg-red-500'];
    const departamentosArray = Object.entries(departamentos)
        .sort((a, b) => b[1] - a[1])
        .slice(0, 8); // Limita a 8 departamentos

    const container = document.getElementById('distribuicao-departamento');
    container.innerHTML = '';

    departamentosArray.forEach((entry, index) => {
        const [departamento, total] = entry;
        const percentual = ((total / totalGeral) * 100).toFixed(1);
        const cor = cores[index % cores.length];

        const div = document.createElement('div');
        div.innerHTML = `
            <div class="flex items-center justify-between mb-2">
                <span class="text-sm text-foreground">${departamento}</span>
                <span class="text-sm font-medium text-foreground">${percentual}%</span>
            </div>
            <div class="w-full bg-muted rounded-full h-2">
                <div class="${cor} h-2 rounded-full" style="width: ${percentual}%"></div>
            </div>
        `;
        container.appendChild(div);
    });
}

function atualizarTabelaRelatorio() {
    const tbody = document.getElementById('tabela-relatorio');
    tbody.innerHTML = '';

    folhas.forEach(folha => {
        const row = document.createElement('tr');
        row.classList.add('border-b', 'border-border', 'hover:bg-muted');
        
        const inss = folha.valorINSS || 0;
        const irrf = folha.valorIRRF || 0;
        const proventos = folha.totalProventos || 0;

        console.log(folha);

        row.innerHTML = `
            <td class="py-3 px-4 text-sm text-foreground">${folha.funcionario.nome}</td>
            <td class="py-3 px-4 text-sm text-foreground">${folha.funcionario.cargo}</td>
            <td class="py-3 px-4 text-sm text-foreground">R$ ${folha.funcionario.financeiro.salarioBruto.toFixed(2)}</td>
            <td class="py-3 px-4 text-sm text-destructive">R$ ${inss.toFixed(2)}</td>
            <td class="py-3 px-4 text-sm text-destructive">R$ ${irrf.toFixed(2)}</td>
            <td class="py-3 px-4 text-sm text-green-600">R$ ${folha.insalubridade.toFixed(2)}</td>
            <td class="py-3 px-4 text-sm text-green-600">R$ ${folha.periculosidade.toFixed(2)}</td>
            <td class="py-3 px-4 text-sm text-green-600">R$ ${folha.proventoValeTransporte.toFixed(2)}</td>
            <td class="py-3 px-4 text-sm text-green-600">R$ ${folha.valeAlimentacao.toFixed(2)}</td>
            <td class="py-3 px-4 text-sm text-green-600">R$ ${folha.salarioFamilia.toFixed(2)}</td>
            <td class="py-3 px-4 text-sm font-medium text-foreground">R$ ${folha.salarioLiquido.toFixed(2)}</td>
        `;
        tbody.appendChild(row);
    });
}

document.addEventListener('DOMContentLoaded', async function () {
    if (!localStorage.getItem('token')) {
        window.location.href = 'login.html';
        return;
    }

    const role = getRoleByToken();
    console.log('Role do usuário:', role);

    if(role !== 'ADMIN'){
        document.getElementById('overview-cards').style.display = 'none';
        document.getElementById('charts-section').style.display = 'none';
    }

    await carregarFolhas();
});