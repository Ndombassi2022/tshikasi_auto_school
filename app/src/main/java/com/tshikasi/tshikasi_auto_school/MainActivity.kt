package com.tshikasi.tshikasi_auto_school

/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tshikasi_auto_schoolTheme {
                var currentScreen by remember { mutableStateOf("splash") }
                var selectedLesson by remember { mutableStateOf<VideoLesson?>(null) }
                var selectedSubject by remember { mutableStateOf<Subject?>(null) }

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
*/

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.tshikasi.tshikasi_auto_school.domain.model.LiveSessionModel
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
import com.tshikasi.tshikasi_auto_school.presentation.pages.live_stream.CreateLivePage
import com.tshikasi.tshikasi_auto_school.presentation.pages.live_stream.LiveStreamListPage
import com.tshikasi.tshikasi_auto_school.presentation.pages.live_stream.LiveStreamPlayerPage
import com.tshikasi.tshikasi_auto_school.presentation.pages.school.SchoolListPage
import com.tshikasi.tshikasi_auto_school.ui.theme.Tshikasi_auto_schoolTheme
/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tshikasi_auto_schoolTheme {
                var currentScreen by remember { mutableStateOf("splash") }
                var selectedLesson by remember { mutableStateOf<VideoLesson?>(null) }
                var selectedSubject by remember { mutableStateOf<Subject?>(null) }

                // ✅ NOVO: Estado para LiveStream
                var selectedLive by remember { mutableStateOf<LiveSessionModel?>(null) }

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
                                selectedSubject = subject
                                currentScreen = "subject_detail"
                            },
                            onNavigateToVideoLesson = { lesson ->
                                selectedLesson = lesson
                                currentScreen = "video_player"
                            },
                            // ✅ NOVO: Navegar para Lives
                            onNavigateToLives = {
                                currentScreen = "live_list"
                            }
                        )
                    }

                    "subject_detail" -> {
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
                        selectedLesson?.let { lesson ->
                            VideoPlayerPage(
                                lesson = lesson,
                                onBackClick = {
                                    currentScreen = "subject_detail"
                                }
                            )
                        }
                    }

                    // ✅ NOVO: LIVESTREAM SCREENS

                    "live_list" -> {
                        LiveStreamListPage(
                            onLiveClick = { live ->
                                selectedLive = live
                                currentScreen = "live_player"
                            },
                            onCreateLive = {
                                currentScreen = "create_live"
                            }
                        )
                    }

                    "live_player" -> {
                        selectedLive?.let { live ->
                            LiveStreamPlayerPage(
                                session = live,
                                onBackClick = {
                                    currentScreen = "live_list"
                                }
                            )
                        }
                    }

                    "create_live" -> {
                        CreateLivePage(
                            onBackClick = {
                                currentScreen = "live_list"
                            },
                            onCreateClick = { request ->
                                // TODO: Chamar ViewModel para criar live
                                // viewModel.createLiveSession(request)
                                currentScreen = "live_list"
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

                // ✅ NOVO: Estado para LiveStream
                var selectedLive by remember { mutableStateOf<LiveSessionModel?>(null) }

                when (currentScreen) {
                    "splash" -> {
                        SplashScreen(
                            onSplashFinished = { currentScreen = "welcome" }
                        )
                    }

                    "welcome" -> {
                        WelcomePage(
                            onGetStarted = { currentScreen = "register" },
                            onLogin = { currentScreen = "login" },
                            onNavigateToSchoolListPage = {currentScreen = "school_list"}
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
                    "school_list" -> {
                        // Implementar SchoolListPage se necessário
                         SchoolListPage(
                             onNavigateToLocationGuidePageClick = { /*TODO*/ },
                             onPopupBack = { /*TODO*/ },
                             onNavigateToLocalServiceListPage = { /*TODO*/ },
                             onNavigateToSchoolAddPage = {}

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
                                selectedSubject = subject
                                currentScreen = "subject_detail"
                            },
                            onNavigateToVideoLesson = { lesson ->
                                selectedLesson = lesson
                                currentScreen = "video_player"
                            },
                            // ✅ NOVO: Navegar para Lives
                            onNavigateToLives = {
                                currentScreen = "live_list"
                            }
                        )
                    }

                    "subject_detail" -> {
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
                        selectedLesson?.let { lesson ->
                            VideoPlayerPage(
                                lesson = lesson,
                                onBackClick = {
                                    currentScreen = "subject_detail"
                                }
                            )
                        }
                    }

                    // ✅ NOVO: LIVESTREAM SCREENS

                    "live_list" -> {
                        LiveStreamListPage(
                            onLiveClick = { live ->
                                selectedLive = live
                                currentScreen = "live_player"
                            },
                            onCreateLive = {
                                currentScreen = "create_live"
                            },
                            onBackClick = {
                                // Voltar para home
                                currentScreen = "home"
                            }
                        )
                    }

                    "live_player" -> {
                        selectedLive?.let { live ->
                            LiveStreamPlayerPage(
                                session = live,
                                onBackClick = {
                                    currentScreen = "live_list"
                                }
                            )
                        }
                    }

                    "create_live" -> {
                        CreateLivePage(
                            onBackClick = {
                                currentScreen = "live_list"
                            },
                            onCreateClick = { request ->
                                // TODO: Chamar ViewModel para criar live
                                // viewModel.createLiveSession(request)
                                currentScreen = "live_list"
                            }
                        )
                    }
                }
            }
        }
    }
}