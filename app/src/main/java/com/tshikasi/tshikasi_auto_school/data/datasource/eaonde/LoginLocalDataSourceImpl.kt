package com.tshikasi.tshikasi_auto_school.data.datasource.eaonde

import com.tshikasi.tshikasi_auto_school.TshikasiAutoSchool
import com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde.LoginLocalDataSource
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalModel
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LoginLocalModel
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order

/*
class LoginLocalDataSourceImpl : LoginLocalDataSource {
    val db = Firebase.firestore
    override suspend fun createLoginLocal(loginLocalModel: LoginLocalModel): LoginLocalModel {
        return suspendCoroutine { continuation ->
            db.collection("tb_login_local")
                .document(loginLocalModel.id)
                .set(loginLocalModel)
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(loginLocalModel))
                }
                .addOnFailureListener{exception ->
                    continuation.resumeWith(Result.failure(exception))
                }
        }
    }

    override suspend fun  getLoginLocalByReferenceAndUserNameAndPassword(reference: String, userName : String, password:String): LoginLocalModel {
        return  suspendCoroutine {continuation ->
            val loginLocalModelReference = db.collection("tb_login_local").whereEqualTo("reference", reference).whereEqualTo("userName",userName).whereEqualTo("password",password)
            loginLocalModelReference.get().addOnSuccessListener {querySnapshot ->
                var loginLocalModel= mutableStateOf(LoginLocalModel())
                for (loginLocal in querySnapshot){
                    loginLocal.toObject(LoginLocalModel::class.java).run {
                        loginLocalModel.value =this
                    }
                }
                continuation.resumeWith(Result.success(loginLocalModel.value))
            }
            loginLocalModelReference.get().addOnFailureListener{
                continuation.resumeWith(Result.failure(it))
            }
        }
    }
}
*/
/*
interface LoginLocalApi {

    @POST("v1/loginLocal")
    suspend fun createLoginLocal(@Body loginLocalModel: LoginLocalModel): LoginLocalModel

    @GET("v1/loginLocal/getReferenceUseNamePassword/{reference}/{userName}/{password}")
    suspend fun getLoginLocalByReferenceAndUserNameAndPassword(
        @Path("reference") reference: String,
        @Path("userName") userName: String,
        @Path("password") password: String
        ): LoginLocalModel
}
/*
class LoginLocalDataSourceImpl : LoginLocalDataSource  {

    private val api: LoginLocalApi by lazy {
        ApiClientEaonde.createService(LoginLocalApi::class.java)
    }

    override suspend fun createLoginLocal(loginLocalModel: LoginLocalModel): LoginLocalModel {
        return suspendCoroutine { continuation ->
            try {

                kotlinx.coroutines.GlobalScope.launch {
                    try {
                        val result = api.createLoginLocal(loginLocalModel)
                        continuation.resumeWith(Result.success(result))

                    } catch (e: Exception) {
                        continuation.resumeWith(Result.failure(e))

                    }
                }
            } catch (e: Exception) {
                continuation.resumeWith(Result.failure(e))

            }
        }
    }

    override suspend fun getLoginLocalByReferenceAndUserNameAndPassword(
        reference: String,
        userName: String,
        password: String
    ): LoginLocalModel {
        return suspendCoroutine { continuation ->
            try {
                // Lançamos uma coroutine separada para chamar o Retrofit
                kotlinx.coroutines.GlobalScope.launch {
                    try {
                        val response = api.getLoginLocalByReferenceAndUserNameAndPassword(
                            reference = reference,
                            userName = userName,
                            password = password
                        )
                        continuation.resumeWith(Result.success(response))

                        println("Response: $response")
                    } catch (e: Exception) {
                        continuation.resumeWith(Result.failure(e))
                    }
                }
            } catch (e: Exception) {
                continuation.resumeWith(Result.failure(e))
            }
        }
    }

}

*/

class LoginLocalDataSourceImpl : LoginLocalDataSource{

    private val api: LoginLocalApi by lazy {
        ApiClientEaonde.createService(LoginLocalApi::class.java)
    }
    override suspend fun createLoginLocal(loginLocalModel: LoginLocalModel): LoginLocalModel {
        return try {
            val response =  api.createLoginLocal(loginLocalModel = loginLocalModel)
            response
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            LoginLocalModel()
        }
    }

    override suspend fun getLoginLocalByReferenceAndUserNameAndPassword(
        reference: String,
        userName: String,
        password: String
    ): LoginLocalModel {
        return try {
            api.getLoginLocalByReferenceAndUserNameAndPassword(reference = reference, userName = userName, password = password)
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            LoginLocalModel()
        }
    }

}
*/

class LoginLocalDataSourceImpl : LoginLocalDataSource {
    private val client = TshikasiAutoSchool.supabase
    override suspend fun createLoginLocal(loginLocalModel: LoginLocalModel): LoginLocalModel {
        return try {
            client.from("tb_login_local")
                .insert(loginLocalModel)
               client.postgrest.from("tb_login_local")
                .select {
                    order("id", Order.DESCENDING)
                    limit(1)
                }
                .decodeSingle<LoginLocalModel>()
        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }


    override suspend fun getLoginLocalByReferenceAndUserNameAndPassword(
        reference: String,
        userName: String,
        password: String
    ): LoginLocalModel {

        return try {
            val loginLocal = client.postgrest.from("tb_login_local")
                .select {
                    filter {
                        eq("reference", reference)
                        eq("password", password)
                        eq("user_name", userName)
                    }
                }
                .decodeSingle<LoginLocalModel>()

            val local = client.postgrest["db_eaonde", "tb_local"]
                .select {
                    filter { eq("id", loginLocal.localId) }
                }
                .decodeSingleOrNull<LocalModel>()

            // Retornar usuário com relacionamentos
            loginLocal.copy(

                    local = local!!
                )




        } catch (e: Exception) {
            println("Erro: ${e.message}")
            throw  e
        }
    }

}