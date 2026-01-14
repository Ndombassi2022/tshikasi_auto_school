package com.tshikasi.tshikasi_auto_school

/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tshikasi_auto_schoolTheme {
                var currentScreen by remember { mutableStateOf("splash") }

                when (currentScreen) {
                    "splash" -> {
                        SplashScreen(
                            onSplashFinished = { currentScreen = "welcome" }
                        )
                    }
                    "welcome" -> {
                        WelcomePage(
                            onGetStarted = { currentScreen = "register" },
                            onLogin = { currentScreen = "login" }
                        )
                    }
                    "login" -> {
                        // LoginScreen (vamos criar a seguir)
                        Text("Login Screen")
                    }
                    "register" -> {
                        // RegisterScreen
                        Text("Register Screen")
                    }
                }
            }
        }
    }
}
*/




import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tshikasi.tshikasi_auto_school.presentation.pages.ForgotPasswordPage
import com.tshikasi.tshikasi_auto_school.presentation.pages.HomePage
import com.tshikasi.tshikasi_auto_school.presentation.pages.LoginPage
import com.tshikasi.tshikasi_auto_school.presentation.pages.SignUpPage
import com.tshikasi.tshikasi_auto_school.presentation.pages.SplashScreen
import com.tshikasi.tshikasi_auto_school.presentation.pages.Subject
import com.tshikasi.tshikasi_auto_school.presentation.pages.WelcomePage
import com.tshikasi.tshikasi_auto_school.presentation.pages.classroom.SubjectDetailPage
import com.tshikasi.tshikasi_auto_school.presentation.pages.classroom.VideoLesson
import com.tshikasi.tshikasi_auto_school.presentation.pages.classroom.VideoPlayerPage
import com.tshikasi.tshikasi_auto_school.ui.theme.Tshikasi_auto_schoolTheme
/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tshikasi_auto_schoolTheme {
                var currentScreen by remember { mutableStateOf("splash") }

                when (currentScreen) {
                    "splash" -> {
                        SplashScreen(
                            onSplashFinished = { currentScreen = "welcome" }
                        )
                    }

                    "welcome" -> {
                        WelcomePage(
                            onGetStarted = { currentScreen = "register" },
                            onLogin = { currentScreen = "login" }
                        )
                    }

                    "login" -> {
                        LoginPage(
                            onLoginSuccess = {
                                // Navegar para a tela principal
                                currentScreen = "home"
                            },
                            onSignUpClick = {
                                currentScreen = "register"
                            },
                            onForgotPasswordClick = {
                                currentScreen = "forgot_password"
                            },
                            onBackClick = {
                                currentScreen = "welcome"
                            }
                        )
                    }

                    "register" -> {
                        // SignUpPage (próxima a criar)
                        SignUpPage(
                            onSignUpSuccess = {
                                currentScreen = "home"
                            },
                            onLoginClick = {
                                currentScreen = "login"
                            },
                            onBackClick = {
                                currentScreen = "welcome"
                            }
                        )
                    }

                    "forgot_password" -> {
                        ForgotPasswordPage(
                            onBackClick = {
                                currentScreen = "login"
                            },
                            onResetSuccess = {
                                currentScreen = "login"
                            }
                        )
                    }

                    "home" -> {
                        // Tela principal após login
                        HomePage(
                            onLogout = {
                                currentScreen = "welcome"
                            }
                        )
                    }
                }
            }
        }
    }
}
*/
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tshikasi_auto_schoolTheme {
                var currentScreen by remember { mutableStateOf("splash") }
                var selectedLesson by remember { mutableStateOf<VideoLesson?>(null) }
                var selectedSubject by remember { mutableStateOf<Subject?>(null) }
                /*
                when (currentScreen) {
                    "splash" -> {
                        SplashScreen(
                            onSplashFinished = { currentScreen = "welcome" }
                        )
                    }

                    "welcome" -> {
                        WelcomePage(
                            onGetStarted = { currentScreen = "register" },
                            onLogin = { currentScreen = "login" }
                        )
                    }

                    "login" -> {
                        LoginPage(
                            onLoginSuccess = { currentScreen = "home" },
                            onSignUpClick = { currentScreen = "register" },
                            onForgotPasswordClick = { currentScreen = "forgot_password" },
                            onBackClick = { currentScreen = "welcome" }
                        )
                    }

                    "register" -> {
                        SignUpPage(
                            onSignUpSuccess = { currentScreen = "home" },
                            onLoginClick = { currentScreen = "login" },
                            onBackClick = { currentScreen = "welcome" }
                        )
                    }

                    "forgot_password" -> {
                        ForgotPasswordPage(
                            onBackClick = { currentScreen = "login" },
                            onResetSuccess = { currentScreen = "login" }
                        )
                    }

                    "home" -> {
                        HomePage(
                            onLogout = { currentScreen = "welcome" },
                            onNavigateToVideoLesson = { lesson ->
                                // ✅ Quando clicar em um vídeo
                                selectedLesson = lesson
                                currentScreen = "video_player"
                            },
                            onSubjectClick = { subject -> // ✅ ADICIONAR
                                selectedSubject = subject
                                currentScreen = "video_player"
                            }
                        )
                    }

                    "video_player" -> {
                        // ✅ Tela do player de vídeo
                        selectedLesson?.let { lesson ->
                            VideoPlayerPage(
                                lesson = lesson,
                                onBackClick = { currentScreen = "home" }
                            )
                        }
                    }
                }
                */
                when (currentScreen) {
                    "splash" -> {
                        SplashScreen(
                            onSplashFinished = { currentScreen = "welcome" }
                        )
                    }

                    "welcome" -> {
                        WelcomePage(
                            onGetStarted = { currentScreen = "register" },
                            onLogin = { currentScreen = "login" }
                        )
                    }

                    "login" -> {
                        LoginPage(
                            onLoginSuccess = { currentScreen = "home" },
                            onSignUpClick = { currentScreen = "register" },
                            onForgotPasswordClick = { currentScreen = "forgot_password" },
                            onBackClick = { currentScreen = "welcome" }
                        )
                    }

                    "register" -> {
                        SignUpPage(
                            onSignUpSuccess = { currentScreen = "home" },
                            onLoginClick = { currentScreen = "login" },
                            onBackClick = { currentScreen = "welcome" }
                        )
                    }

                    "forgot_password" -> {
                        ForgotPasswordPage(
                            onBackClick = { currentScreen = "login" },
                            onResetSuccess = { currentScreen = "login" }
                        )
                    }

                    "home" -> {
                        HomePage(
                            onLogout = { currentScreen = "welcome" },
                            onSubjectClick = { subject ->
                                // ✅ Quando clicar numa disciplina no HomePage
                                selectedSubject = subject
                                currentScreen = "subject_detail"
                            },

                            onNavigateToVideoLesson = { lesson ->
                                // ✅ Quando clicar numa aula no HomePage (continuar assistir)
                                selectedLesson = lesson
                                currentScreen = "video_player"
                            }
                        )
                    }

                    "subject_detail" -> {
                        // ✅ Página com todas as aulas da disciplina
                        selectedSubject?.let { subject ->
                            SubjectDetailPage(
                                subject = subject,
                                onBackClick = {
                                    currentScreen = "home"
                                },
                                onVideoClick = { lesson ->
                                    selectedLesson = lesson
                                    currentScreen = "video_player"
                                }
                            )
                        }
                    }

                    "video_player" -> {
                        // ✅ Player de vídeo
                        selectedLesson?.let { lesson ->
                            VideoPlayerPage(
                                lesson = lesson,
                                onBackClick = {
                                    // Volta para a página de detalhes da disciplina
                                    currentScreen = "subject_detail"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Tshikasi_auto_schoolTheme {
        Greeting("Android")
    }
}

