import { findAllFuncionarios } from '../../services/api-service.js';
import { getUserName } from '../../services/utils.js';

var funcionarios;
var paginas;

async function atualizarNomeUsuario() {
    const usernameSpan = document.getElementById('username');
    let nome = await getUserName();
    usernameSpan.textContent = nome;
}

function criarTabela() {
    const tabelaContainer = document.getElementById('funcionarios');

    tabelaContainer.innerHTML = '';

    const tabela = document.createElement('table');
    tabela.classList.add('w-full');
    tabela.innerHTML = `
        <thead class="bg-muted">
            <tr>
              <th class="text-left py-3 px-4 text-sm font-medium text-muted-foreground">Funcionário</th>
              <th class="text-left py-3 px-4 text-sm font-medium text-muted-foreground">Data Admissão</th>
              <th class="text-left py-3 px-4 text-sm font-medium text-muted-foreground">Cargo</th>
              <th class="text-left py-3 px-4 text-sm font-medium text-muted-foreground">Salário bruto</th>
              <th class="text-left py-3 px-4 text-sm font-medium text-muted-foreground">Status</th>
              <th class="text-left py-3 px-4 text-sm font-medium text-muted-foreground">Ações</th>
            </tr>
          </thead>
        <tbody id="tabela-funcionarios">
        </tbody>
    `;

    tabelaContainer.appendChild(tabela);
}

function showNoFuncionariosMessage() {
    const container = document.getElementById('funcionarios');
    if (!container) return;
    container.innerHTML = '';

    const msg = document.createElement('div');
    msg.classList.add('p-6', 'text-center');
    msg.innerHTML = `
        <p class="text-sm text-muted-foreground">Nenhum funcionário encontrado.</p>
        <p class="text-xs text-muted-foreground mt-2">Você pode cadastrar funcionários pelo painel ou clicar em "Atualizar" para tentar novamente.</p>
        <div class="mt-4">
            <button id="btn-refresh-funcionarios" class="px-3 py-1 bg-primary text-white rounded text-sm">Atualizar</button>
        </div>
    `;

    container.appendChild(msg);

    const btn = document.getElementById('btn-refresh-funcionarios');
    if (btn) {
        btn.addEventListener('click', async () => {
            btn.disabled = true;
            btn.textContent = 'Carregando...';
            await carregarFuncionarios();
            btn.disabled = false;
            btn.textContent = 'Atualizar';
        });
    }
}

function randomColor(){
    const colors = ['red', 'blue', 'green', 'yellow', 'purple', 'pink', 'indigo', 'teal', 'orange', 'cyan'];
    const index = Math.floor(Math.random() * colors.length);
    return colors[index];
}

async function carregarFuncionarios(paginaAtual){
    try{
        if(!paginaAtual){ // primeira vez que carrega
            funcionarios = await findAllFuncionarios();
            paginaAtual = 1;

            if (!Array.isArray(funcionarios)) {
                funcionarios = funcionarios ? [funcionarios] : [];
            }

            if(funcionarios.length === 0){
                showNoFuncionariosMessage();
                return;
            }

            criarTabela();

            paginas = Math.ceil(funcionarios.length / 5);
        }

        const tabela = document.getElementById('tabela-funcionarios');
        tabela.innerHTML = '';
        
        const porPagina = 5;
        const inicio = (paginaAtual - 1) * porPagina;
        const fim = inicio + porPagina;

        funcionarios.slice(inicio, fim).forEach(funcionario => {
            console.log(funcionario);

            let status = funcionario.deFerias ? 'Férias' : 'Ativo';
            let nome = funcionario.nome;
            let inicial = '';
            if (nome) {
                const parts = nome.trim().split(/\s+/).filter(Boolean);
                if (parts.length === 1) {
                    inicial = parts[0].charAt(0).toUpperCase();
                } else {
                    const first = parts[0].charAt(0).toUpperCase();
                    const last = parts[parts.length - 1].charAt(0).toUpperCase();
                    inicial = first + last;
                }
            }

            const row = document.createElement('tr');
            row.classList.add('border-b', 'border-border', 'hover:bg-muted');
            row.innerHTML = `
                <td class="py-3 px-4">
                    <div class="flex items-center gap-3">
                    <div class="w-10 h-10 bg-${randomColor()}-500 rounded-full flex items-center justify-center text-white font-medium">${inicial}</div>
                    <div>
                        <p class="text-sm font-medium text-foreground">${nome}</p>
                        <p class="text-xs text-muted-foreground">${funcionario.login}</p>
                    </div>
                    </div>
                </td>
                <td class="py-3 px-4 text-sm text-foreground">${funcionario.dataAdmissao}</td>
                <td class="py-3 px-4 text-sm text-foreground">${funcionario.cargo}</td>
                <td class="py-3 px-4 text-sm font-medium text-foreground">R$ ${(funcionario.financeiro && funcionario.financeiro.salarioBruto) ? funcionario.financeiro.salarioBruto.toFixed(2) : '0.00'}</td>
                <td class="py-3 px-4">
                    <span class="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-green-100 text-green-700">${status}</span>
                </td>
                <td class="py-3 px-4">
                    <button class="text-primary hover:text-primary-dark text-sm font-medium">Editar</button>
                </td>
            `;
            tabela.appendChild(row);
        });
        
    } catch (error){

    }
}

async function carregarPaginacao() {
    const paginacaoDiv = document.getElementById('pagination');
    let paginaAtual = 1;

    paginacaoDiv.innerHTML = '';

    paginacaoDiv.innerHTML =
    `
        <p class="text-sm text-muted-foreground">Exibindo páginas 1-${paginas} de um total de ${funcionarios.length} funcionários</p>
        <div id="pag-buttons" class="flex items-center gap-2">
            <button id="btn-anterior" class="px-3 py-1 border border-border rounded-lg text-sm text-muted-foreground hover:bg-muted">Anterior</button>
            <button data-pagina="1" class="px-3 py-1 bg-primary text-white rounded-lg text-sm active">1</button>
            <button id="btn-proximo" class="px-3 py-1 border border-border rounded-lg text-sm text-muted-foreground hover:bg-muted">Próximo</button>
        </div>
    `;

    const pagButtons = document.getElementById('pag-buttons');
    const nextButton = pagButtons.querySelector('button:last-child');

    if(paginas > 1){
        for(let i = 2; i <= paginas; i++) {
            const button = document.createElement('button');
            button.classList.add('px-3', 'py-1', 'border', 'border-border', 'rounded-lg', 'text-sm', 'text-muted-foreground', 'hover:bg-muted', 'pagination-btn');
            button.textContent = i;
            button.dataset.pagina = i;
            pagButtons.insertBefore(button, nextButton);
        }
    }

    const atualizarPaginacao = (novaPagina) => {
        paginaAtual = novaPagina;

        document.querySelectorAll('[data-pagina]').forEach(btn => {
            btn.classList.remove('active', 'bg-primary', 'text-white');
            btn.classList.add('border', 'border-border', 'text-muted-foreground', 'hover:bg-muted');
        });

        const btnAtual = document.querySelector(`[data-pagina="${novaPagina}"]`);
        if (btnAtual) {
            btnAtual.classList.add('active', 'bg-primary', 'text-white');
            btnAtual.classList.remove('border', 'border-border', 'text-muted-foreground', 'hover:bg-muted');
        }

        const btnAnterior = document.getElementById('btn-anterior');
        if (paginaAtual === 1) {
            btnAnterior.disabled = true;
            btnAnterior.classList.add('opacity-50', 'cursor-not-allowed');
        } else {
            btnAnterior.disabled = false;
            btnAnterior.classList.remove('opacity-50', 'cursor-not-allowed');
        }

        const btnProximo = document.getElementById('btn-proximo');
        if (paginaAtual === paginas) {
            btnProximo.disabled = true;
            btnProximo.classList.add('opacity-50', 'cursor-not-allowed');
        } else {
            btnProximo.disabled = false;
            btnProximo.classList.remove('opacity-50', 'cursor-not-allowed');
        }

        carregarFuncionarios(paginaAtual);
    };

    pagButtons.addEventListener('click', (e) => {
        const btn = e.target.closest('button');
        if (!btn) return;

        const pagina = btn.dataset.pagina;
        if (pagina) {
            atualizarPaginacao(parseInt(pagina));
        }
    });

    document.getElementById('btn-anterior').addEventListener('click', () => {
        if (paginaAtual > 1) {
            atualizarPaginacao(paginaAtual - 1);
        }
    });

    document.getElementById('btn-proximo').addEventListener('click', () => {
        if (paginaAtual < paginas) {
            atualizarPaginacao(paginaAtual + 1);
        }
    });

    atualizarPaginacao(1);
}

document.addEventListener('DOMContentLoaded', async function () {
    if (!localStorage.getItem('token')) {
      window.location.href = 'login.html';
    }

    await atualizarNomeUsuario();

    await carregarFuncionarios();

    await carregarPaginacao();
});