package com.kuteam6.homept.restservice

import android.annotation.SuppressLint
import android.util.Log
import android.widget.TextView
import com.google.gson.annotations.SerializedName
import com.kuteam6.homept.restservice.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

interface ApiService {

    @POST("/user/checkId")
    suspend fun checkIdDupicate(
        @Body checkIdRequest: CheckIdRequest
    ): Response<CheckIdResponse?>

    @POST("/user/register")
    suspend fun registerUser(
        @Body registerRequest: RegisterRequest
    ): Response<RegisterResponse?>

    @POST("/user/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse?>

    @POST("/user/unregister")
    suspend fun unRegisterUser(
        @Body unRegisterRequest: UnRegisterRequest
    ): Response<UnRegisterResponse?>




}

// 해당 클래스는 싱글톤패턴 클래스로 getinstance로 가져와야함
object ApiManager {

    // 카페 데이터 baseURl
    private val apiBaseUrl = "http://ec2-3-145-70-159.us-east-2.compute.amazonaws.com:3000"


    private val apiclient = OkHttpClient.Builder().build()
    var apiService: ApiService

    init {

        apiService = Retrofit.Builder()
            .baseUrl(apiBaseUrl)
            .client(apiclient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    /**
     * ID중복확인 함수 단순 중복시 true리턴 중복아닐시 false리턴
     */
    suspend fun checkIdDupicated(id:String): Boolean= withContext(Dispatchers.IO){

        try {
            val response= apiService.checkIdDupicate(
                CheckIdRequest(id)
            );

            if(response.isSuccessful){
                Log.d("TAG","[check ID] 정상 작동 : checkid= ${id},result => ${response.body()?.isDuplicated}");

                var  result =response.body()?.isDuplicated
                return@withContext result!!;

            }else{
                true
            }
        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
            true
        }

    }

    /**
     * 회원가입 함수 정의되어잇는 Userdata를 상속한 TrainerData, TraineeData를 매개로 받아
     * 회원가입에 성공하면 uid,(trainerid or traineeid)가 넣어져 있는 userdata를 리턴 실패시 null리턴
     */
    suspend fun register(userdata: UserData): UserData?= withContext(Dispatchers.IO){
        if(userdata.id ==""|| userdata.password=="") {
            return@withContext null;
        }
        try {
            val response= apiService.registerUser(
                createRegisterReq(userdata)
            );

            if(response.isSuccessful){
                Log.d("TAG","[register] 정상 작동 : ${userdata}");

                var resultcode =response.body()?.code
                if(resultcode==200){
                    userdata.uid= response.body()?.uid!!
                    if(userdata is TrainerData)
                        userdata.trainerid= response.body()!!.subuid!!
                    else if(userdata is TraineeData){
                        userdata.traineeid= response.body()!!.subuid!!

                    }
                    return@withContext userdata
                }
                else{
                    Log.d("TAG","register api 오류 ${response.body()?.message}")
                }

            }else{
                Log.d("TAG","register api 오류 ${response.body()?.message}")

            }
        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
        }

        null
    }


    /**
     * 로그인 함수 id,password를 매개로 받아
     * 로그인에 성공하면 UserData (TrainerData or TraineeData)리턴  아이디 비밀번호 오류시 null리턴
     */
    suspend fun login(id:String,password:String): UserData?= withContext(Dispatchers.IO){

        try {
            val response= apiService.loginUser(
                LoginRequest(id,password)
            );
            if(response.isSuccessful){
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                var resultcode =response.body()?.code
                if(resultcode==200 &&response.body()?.userdata !=null){
                    var userdata = response.body()?.userdata!!
                    var subdata=response.body()?.subuserdata!!
                    var birthdate=LocalDate.parse(userdata.birth, formatter)
                    if(userdata.role=="1"){ // 트레이니
                        return@withContext TraineeData(userdata.name,userdata.login_id,userdata.password, userdata.gender,birthdate,true,userdata.usercategory,
                            subdata.description!!,subdata.trainee_id!!  ).apply {
                                this.uid=userdata.uid
                        }

                    }else{ //트레이너
                        return@withContext TrainerData(userdata.name,userdata.login_id,userdata.password, userdata.gender,birthdate,true,userdata.usercategory,
                            subdata.career!!,subdata.lesson!!,subdata.trainer_id!!  ).apply {
                            this.uid=userdata.uid
                        }
                    }

                }
                else{
                    Log.d("TAG","Login 실패 ${response.body()?.message}")
                }

            }else{
                Log.d("TAG","Login api 오류 ${response.body()?.message}")
            }
        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
        }
        null
    }


    /**
     * 회원 탈퇴 id,password를 매개로 받아
     * 탈퇴에 성공하면 true, 해당 id 비번이 없어 탈퇴에 실패하면 false
     */
    suspend fun unRegister(id:String,password:String): Boolean= withContext(Dispatchers.IO){

        try {
            val response= apiService.unRegisterUser(
                UnRegisterRequest(id,password)
            );
            if(response.isSuccessful){

                var resultcode =response.body()?.code
                if(resultcode==200) {
                    return@withContext response.body()?.isDeleted!!
                }
                else{
                    Log.d("TAG","회원 탈퇴 실패 ${response.body()?.message}")
                }

            }else{
                Log.d("TAG","회원 탈퇴 오류 ${response.body()?.message}")
            }
        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
        }
        false
    }

}


