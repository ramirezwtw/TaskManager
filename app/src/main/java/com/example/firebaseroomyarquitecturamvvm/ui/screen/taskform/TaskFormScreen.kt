package com.example.firebaseroomyarquitecturamvvm.ui.screen.taskform

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.firebaseroomyarquitecturamvvm.ui.state.OperationState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFormScreen(
    onNavigateBack: () -> Unit,
    viewModel: TaskFormViewModel = hiltViewModel()
) {
    val task by viewModel.task.collectAsStateWithLifecycle()
    val operationState by viewModel.operationState.collectAsStateWithLifecycle()

    LaunchedEffect(operationState) {
        if (operationState is OperationState.Success) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (task.id.isEmpty()) "Nueva Tarea" else "Editar Tarea") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = task.title,
                onValueChange = { viewModel.onTitleChange(it) },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = task.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            if (operationState is OperationState.Loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.saveTask() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = task.title.isNotBlank()
                ) {
                    Text("Guardar en la Nube")
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { viewModel.saveAsDraft() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = task.title.isNotBlank()
                ) {
                    Text("Guardar como Borrador")
                }
            }
            
            if (operationState is OperationState.Error) {
                Text(text = (operationState as OperationState.Error).message, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}