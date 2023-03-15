package io.github.akiomik.seiun.ui.registration

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.akiomik.seiun.R
import io.github.akiomik.seiun.SeiunApplication
import io.github.akiomik.seiun.datastores.Credential
import io.github.akiomik.seiun.datastores.Session
import io.github.akiomik.seiun.ui.theme.Red700
import io.github.akiomik.seiun.viewmodel.RegistrationViewModel

@Composable
fun RegistrationTitle() {
    Text(
        text = stringResource(id = R.string.registration_title),
        fontSize = 23.sp,
        modifier = Modifier.padding(20.dp)
    )
}

@Composable
fun RegistrationForm(onRegistrationSuccess: () -> Unit) {
    val viewModel: RegistrationViewModel = viewModel()
    var serviceProvider by remember { mutableStateOf("bsky.social") }
    var email by remember { mutableStateOf("") }
    var handle by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var inviteCode by remember { mutableStateOf("") }
    var valid by remember {
        mutableStateOf(
            viewModel.isRegisterParamValid(
                serviceProvider,
                email,
                handle,
                password
            )
        )
    }
    var errorMessage by remember { mutableStateOf("") }

    Column {
        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = Red700, modifier = Modifier.padding(20.dp))
        }

        TextField(
            value = serviceProvider,
            onValueChange = {
                serviceProvider = it
                valid = viewModel.isRegisterParamValid(serviceProvider, email, handle, password)
            },
            label = { Text(stringResource(id = R.string.service_provider)) },
            placeholder = { Text("bsky.social") },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            singleLine = true
        )

        TextField(
            value = email,
            onValueChange = {
                email = it
                valid = viewModel.isRegisterParamValid(serviceProvider, email, handle, password)
            },
            label = { Text(stringResource(id = R.string.registration_email)) },
            placeholder = { Text(text = stringResource(id = R.string.registration_email_placeholder)) },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            singleLine = true
        )

        TextField(
            value = handle,
            onValueChange = {
                handle = it
                valid = viewModel.isRegisterParamValid(serviceProvider, email, handle, password)
            },
            label = { Text(stringResource(id = R.string.registration_handle)) },
            placeholder = { Text(text = stringResource(id = R.string.registration_handle_placeholder)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
            prefix = { Text(text = "@") },
            suffix = { Text(text = ".$serviceProvider") },
            maxLines = 1,
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            singleLine = true
        )

        TextField(
            value = password,
            onValueChange = {
                password = it
                valid = viewModel.isRegisterParamValid(serviceProvider, email, handle, password)
            },
            label = { Text(stringResource(id = R.string.registration_password)) },
            maxLines = 1,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            singleLine = true
        )

        TextField(
            value = inviteCode,
            onValueChange = {
                inviteCode = it
                valid = viewModel.isRegisterParamValid(serviceProvider, email, handle, password)
            },
            label = { Text(stringResource(id = R.string.registration_invite_code)) },
            placeholder = { Text(text = "bsky.social-XXXXXX") },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            singleLine = true
        )

        ElevatedButton(
            onClick = {
                Log.d(SeiunApplication.TAG, "Create account for $handle on $serviceProvider")

                // NOTE: Init atpClient here using serviceProvider
                SeiunApplication.instance!!.setAtpClient(serviceProvider)

                val userRepository = SeiunApplication.instance!!.userRepository
                userRepository.saveCredential(
                    Credential(
                        serviceProvider,
                        "$handle.$serviceProvider",
                        password
                    )
                )

                viewModel.register(
                    email = email,
                    handle = "$handle.$serviceProvider",
                    password = password,
                    inviteCode = inviteCode,
                    onSuccess = { session ->
                        Log.d(SeiunApplication.TAG, "Create account successful")
                        userRepository.saveSession(Session.fromISession(session))
                        onRegistrationSuccess()
                    },
                    onError = { error ->
                        Log.d(SeiunApplication.TAG, "Login failure: $error")
                        errorMessage = error.message ?: "Failed to create account"
                    }
                )
            },
            enabled = valid,
            modifier = Modifier
                .padding(20.dp)
                .align(alignment = Alignment.End)
        ) {
            Text(stringResource(id = R.string.registration_button))
        }
    }
}

@Composable
fun RegistrationScreen(onRegistrationSuccess: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            RegistrationTitle()
            RegistrationForm(onRegistrationSuccess = onRegistrationSuccess)
        }
    }
}
