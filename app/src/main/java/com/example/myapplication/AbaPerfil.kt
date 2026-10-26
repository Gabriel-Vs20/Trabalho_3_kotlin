package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun AbaPerfil(navController: NavHostController, viewModel: MeuViewModel) {

    LaunchedEffect(Unit) {
        viewModel.carregarTudo()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF4F7F6)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier.size(90.dp).background(Color(0xFF009739), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.padding(5.dp))

            Text(
                if (viewModel.nome.isBlank()) "Seu perfil" else viewModel.nome,
                fontSize = 19.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1F2937)
            )
            Text(viewModel.email, fontSize = 13.sp, color = Color(0xFF6B7A74))

            Spacer(modifier = Modifier.padding(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        viewModel.estabelecimentos.size.toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF009739)
                    )
                    Text("Locais", fontSize = 12.sp, color = Color(0xFF6B7A74))
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        viewModel.agendamentos.size.toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF009739)
                    )
                    Text("Agendamentos", fontSize = 12.sp, color = Color(0xFF6B7A74))
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        viewModel.agendamentos.count { it.concluido }.toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF009739)
                    )
                    Text("Concluídos", fontSize = 12.sp, color = Color(0xFF6B7A74))
                }
            }

            Spacer(modifier = Modifier.padding(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {

                TituloSecao("Meus dados")
                Spacer(modifier = Modifier.padding(5.dp))

                CampoTexto(
                    titulo = "NOME COMPLETO",
                    valor = viewModel.nome,
                    placeholder = "Digite seu nome",
                    aoAlterar = { viewModel.alterarNome(it) }
                )
                Spacer(modifier = Modifier.padding(5.dp))

                CampoTexto(
                    titulo = "EMAIL",
                    valor = viewModel.email,
                    placeholder = "exemplo@email.com",
                    aoAlterar = { viewModel.alterarEmail(it) }
                )
                Spacer(modifier = Modifier.padding(5.dp))

                CampoTexto(
                    titulo = "SENHA",
                    valor = viewModel.senha,
                    placeholder = "Mínimo 4 caracteres",
                    aoAlterar = { viewModel.alterarSenha(it) },
                    ehSenha = true
                )
                Spacer(modifier = Modifier.padding(5.dp))

                CampoTexto(
                    titulo = "CPF",
                    valor = viewModel.cpf,
                    placeholder = "000.000.000-00",
                    aoAlterar = { viewModel.alterarCpf(it) }
                )
                Spacer(modifier = Modifier.padding(5.dp))

                CampoTexto(
                    titulo = "ENDEREÇO",
                    valor = viewModel.endereco,
                    placeholder = "Rua, Número, Bairro",
                    aoAlterar = { viewModel.alterarEndereco(it) }
                )
                Spacer(modifier = Modifier.padding(5.dp))

                CampoTexto(
                    titulo = "DATA DE NASC.",
                    valor = viewModel.dataNasc,
                    placeholder = "DD/MM/YYYY",
                    aoAlterar = { viewModel.alterarDataNasc(it) }
                )

                if (viewModel.mensagemPerfil.isNotBlank()) {
                    Spacer(modifier = Modifier.padding(5.dp))
                    CaixaSucesso(viewModel.mensagemPerfil)
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Button(
                    onClick = { viewModel.salvarPerfil() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009739)),
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Salvar alterações", fontWeight = FontWeight.ExtraBold) }

            }

            Spacer(modifier = Modifier.padding(6.dp))

            OutlinedButton(
                onClick = {
                    viewModel.sair()
                    navController.navigate(Rotas.LOGIN) {
                        popUpTo(Rotas.PRINCIPAL) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Sair da conta", color = Color(0xFFC62828), fontWeight = FontWeight.Bold) }

        }
    }

}
