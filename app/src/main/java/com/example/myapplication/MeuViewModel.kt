package com.example.myapplication

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MeuViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val estabelecimentoDao = db.estabelecimentoDAO()
    private val agendamentoDao = db.agendamentoDAO()

    var user by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var nome by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var senha by mutableStateOf("")
        private set

    var cpf by mutableStateOf("")
        private set

    var endereco by mutableStateOf("")
        private set

    var dataNasc by mutableStateOf("")
        private set

    var cadastroFeito by mutableStateOf(false)
        private set

    var erroLogin by mutableStateOf("")
        private set

    var erroCadastro by mutableStateOf("")
        private set

    var mensagemPerfil by mutableStateOf("")
        private set

    var erroEstabelecimento by mutableStateOf("")
        private set

    var erroAgendamento by mutableStateOf("")
        private set

    private var proximoIdNotificacao = 5

    // as listas agora só espelham o que está no banco; quem preenche são as funções carregar...()
    val estabelecimentos = mutableStateListOf<Estabelecimento>()

    val agendamentos = mutableStateListOf<Agendamento>()

    val notificacoes = mutableStateListOf(
        Notificacao(1, "Agendamento confirmado", "A Barbearia A confirmou seu corte de 31/10 às 10:00.", "Hoje, 09:12", false),
        Notificacao(2, "Promoção no Studio Bella", "Hidratação com 20% de desconto até o fim do mês.", "Ontem, 18:40", false),
        Notificacao(3, "Avalie seu atendimento", "Como foi a lavagem na AutoShine? Deixe sua nota.", "2 dias atrás", true),
        Notificacao(4, "Bem-vindo ao Bora Agendar", "Cadastre seus estabelecimentos favoritos e agende em poucos cliques.", "5 dias atrás", true)
    )

    fun alterarUser(valor: String) {
        user = valor
        erroLogin = ""
    }

    fun alterarPassword(valor: String) {
        password = valor
        erroLogin = ""
    }

    fun alterarNome(valor: String) {
        nome = valor
        erroCadastro = ""
        mensagemPerfil = ""
    }

    fun alterarEmail(valor: String) {
        email = valor
        erroCadastro = ""
        mensagemPerfil = ""
    }

    fun alterarSenha(valor: String) {
        senha = valor
        erroCadastro = ""
        mensagemPerfil = ""
    }

    fun alterarCpf(valor: String) {
        cpf = valor
        erroCadastro = ""
        mensagemPerfil = ""
    }

    fun alterarEndereco(valor: String) {
        endereco = valor
        erroCadastro = ""
        mensagemPerfil = ""
    }

    fun alterarDataNasc(valor: String) {
        dataNasc = valor
        erroCadastro = ""
        mensagemPerfil = ""
    }

    fun salvarCadastro(): Boolean {
        if (nome.isBlank() || email.isBlank() || senha.isBlank() || cpf.isBlank() || endereco.isBlank() || dataNasc.isBlank()) {
            erroCadastro = "Preencha todos os campos para continuar"
            return false
        }
        if (senha.length < 4) {
            erroCadastro = "A senha precisa ter pelo menos 4 caracteres"
            return false
        }
        cadastroFeito = true
        erroCadastro = ""
        user = email
        password = ""
        return true
    }

    fun validarLogin(): Boolean {
        if (!cadastroFeito) {
            erroLogin = "Nenhuma conta cadastrada ainda. Cadastre-se primeiro."
            return false
        }
        if (user.isBlank() || password.isBlank()) {
            erroLogin = "Informe usuário e senha"
            return false
        }
        if (user != email || password != senha) {
            erroLogin = "Usuário ou senha incorretos"
            return false
        }
        erroLogin = ""
        return true
    }

    fun salvarPerfil() {
        if (nome.isBlank() || email.isBlank() || senha.isBlank()) {
            mensagemPerfil = "Nome, email e senha não podem ficar vazios"
            return
        }
        mensagemPerfil = "Dados atualizados com sucesso"
    }

    fun sair() {
        user = ""
        password = ""
        erroLogin = ""
        mensagemPerfil = ""
    }

    // ---------- Banco de dados (Room) ----------

    suspend fun carregarEstabelecimentos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val lista = estabelecimentoDao.buscarTodos()
                withContext(Dispatchers.Main) {
                    estabelecimentos.clear()
                    estabelecimentos.addAll(lista)
                }
            } catch (e: Exception) {
                Log.e("Erro ao buscar", "Estabelecimentos: ${e.message}")
            }
        }.join()
    }

    suspend fun carregarAgendamentos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val lista = agendamentoDao.buscarTodos()
                withContext(Dispatchers.Main) {
                    agendamentos.clear()
                    agendamentos.addAll(lista)
                }
            } catch (e: Exception) {
                Log.e("Erro ao buscar", "Agendamentos: ${e.message}")
            }
        }.join()
    }

    suspend fun carregarTudo() {
        carregarEstabelecimentos()
        carregarAgendamentos()
    }

    suspend fun buscarEstabelecimentoPorId(id: Int): Estabelecimento? {
        var item: Estabelecimento? = null
        CoroutineScope(Dispatchers.IO).launch {
            try {
                item = estabelecimentoDao.buscarPorId(id)
            } catch (e: Exception) {
                Log.e("Erro ao buscar", "Estabelecimento $id: ${e.message}")
            }
        }.join()
        return item
    }

    suspend fun buscarAgendamentoPorId(id: Int): Agendamento? {
        var item: Agendamento? = null
        CoroutineScope(Dispatchers.IO).launch {
            try {
                item = agendamentoDao.buscarPorId(id)
            } catch (e: Exception) {
                Log.e("Erro ao buscar", "Agendamento $id: ${e.message}")
            }
        }.join()
        return item
    }

    suspend fun buscarAgendamentosDoEstabelecimento(id: Int): List<Agendamento> {
        var lista: List<Agendamento> = emptyList()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                lista = agendamentoDao.buscarPorEstabelecimento(id)
            } catch (e: Exception) {
                Log.e("Erro ao buscar", "Agendamentos do estabelecimento $id: ${e.message}")
            }
        }.join()
        return lista
    }

    fun adicionarEstabelecimento(nome: String, categoria: String, nota: String, endereco: String): Boolean {
        if (nome.isBlank() || categoria.isBlank()) {
            erroEstabelecimento = "Informe pelo menos nome e categoria"
            return false
        }
        val novo = Estabelecimento(
            nome = nome,
            categoria = categoria,
            nota = if (nota.isBlank()) "0.0" else nota,
            endereco = if (endereco.isBlank()) "Endereço não informado" else endereco
        )
        CoroutineScope(Dispatchers.IO).launch {
            try {
                estabelecimentoDao.inserir(novo)
                carregarEstabelecimentos()
            } catch (e: Exception) {
                Log.e("Erro do Add", "Estabelecimento: ${e.message}")
            }
        }
        erroEstabelecimento = ""
        return true
    }

    fun removerEstabelecimento(item: Estabelecimento) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                agendamentoDao.deletarPorEstabelecimento(item.id)
                estabelecimentoDao.deletar(item)
                carregarTudo()
            } catch (e: Exception) {
                Log.e("Erro ao remover", "Estabelecimento: ${e.message}")
            }
        }
    }

    fun buscarEstabelecimento(id: Int): Estabelecimento? {
        return estabelecimentos.find { it.id == id }
    }

    fun agendamentosDoEstabelecimento(id: Int): List<Agendamento> {
        return agendamentos.filter { it.idEstabelecimento == id }
    }

    fun adicionarAgendamento(idEstabelecimento: Int, servico: String, data: String, hora: String, preco: String): Boolean {
        if (idEstabelecimento == 0) {
            erroAgendamento = "Escolha um estabelecimento"
            return false
        }
        if (servico.isBlank() || data.isBlank() || hora.isBlank()) {
            erroAgendamento = "Preencha serviço, data e hora"
            return false
        }
        val valor = preco.replace(",", ".").toDoubleOrNull()
        if (valor == null) {
            erroAgendamento = "Preço inválido"
            return false
        }
        val novo = Agendamento(
            idEstabelecimento = idEstabelecimento,
            servico = servico,
            data = data,
            hora = hora,
            preco = valor
        )
        CoroutineScope(Dispatchers.IO).launch {
            try {
                agendamentoDao.inserir(novo)
                carregarAgendamentos()
            } catch (e: Exception) {
                Log.e("Erro do Add", "Agendamento: ${e.message}")
            }
        }
        erroAgendamento = ""
        return true
    }

    fun removerAgendamento(item: Agendamento) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                agendamentoDao.deletar(item)
                carregarAgendamentos()
            } catch (e: Exception) {
                Log.e("Erro ao remover", "Agendamento: ${e.message}")
            }
        }
    }

    fun alternarConcluido(item: Agendamento, aoTerminar: () -> Unit = {}) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                agendamentoDao.atualizar(item.copy(concluido = !item.concluido))
                carregarAgendamentos()
                withContext(Dispatchers.Main) { aoTerminar() }
            } catch (e: Exception) {
                Log.e("Erro ao atualizar", "Agendamento: ${e.message}")
            }
        }
    }

    fun atualizarAgendamento(
        item: Agendamento,
        servico: String,
        data: String,
        hora: String,
        preco: String,
        observacao: String,
        aoTerminar: () -> Unit = {}
    ) {
        if (servico.isBlank() || data.isBlank() || hora.isBlank()) {
            erroAgendamento = "Preencha serviço, data e hora"
            return
        }
        val valor = preco.replace(",", ".").toDoubleOrNull()
        if (valor == null) {
            erroAgendamento = "Preço inválido"
            return
        }
        val atualizado = item.copy(
            servico = servico,
            data = data,
            hora = hora,
            preco = valor,
            observacao = observacao
        )
        erroAgendamento = ""
        CoroutineScope(Dispatchers.IO).launch {
            try {
                agendamentoDao.atualizar(atualizado)
                carregarAgendamentos()
                withContext(Dispatchers.Main) { aoTerminar() }
            } catch (e: Exception) {
                Log.e("Erro ao atualizar", "Agendamento: ${e.message}")
            }
        }
    }

    fun limparErroAgendamento() {
        erroAgendamento = ""
    }

    fun limparErroEstabelecimento() {
        erroEstabelecimento = ""
    }

    fun marcarNotificacaoLida(item: Notificacao) {
        val posicao = notificacoes.indexOfFirst { it.id == item.id }
        if (posicao >= 0) {
            notificacoes[posicao] = item.copy(lida = true)
        }
    }

    fun removerNotificacao(item: Notificacao) {
        notificacoes.remove(item)
    }

    fun adicionarNotificacao(titulo: String, mensagem: String) {
        notificacoes.add(0, Notificacao(proximoIdNotificacao, titulo, mensagem, "Agora", false))
        proximoIdNotificacao++
    }

    fun notificacoesNaoLidas(): Int {
        return notificacoes.count { !it.lida }
    }

}
