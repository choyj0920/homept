package com.kuteam6.homept.restservice

import android.util.Log
import com.kuteam6.homept.restservice.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.*
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.File
import java.time.LocalDate
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

    @POST("/user/findpassword/check")
    suspend fun checkFindPassword(
        @Body passwordFindRequest: PasswordFindRequest
    ): Response<PasswordFindResponse?>

    @POST("/user/findpassword/change")
    suspend fun changeFindPassword(
        @Body passwordFindRequest: PasswordFindRequest
    ): Response<PasswordFindResponse?>

    @POST("/search/trainer")
    suspend fun serachTrainer(
        @Body trainerSearchRequest: TrainerSearchRequest
    ): Response<TrainerSearchResponse?>

    @POST("/trainer/getProfile")
    suspend fun getTrainerProfile(
        @Body getTrainerProfileRequest: GetTrainerProfileRequest
    ): Response<GetTrainerProfileResponse?>

    @POST("/user/gethbti")
    suspend fun getHbti(
        @Body getHbtiRequest: GetHbtiRequest
    ): Response<GetHbtiResponse?>
    @POST("/user/sethbti")
    suspend fun setHbti(
        @Body setHbtiRequest: SetHbtiRequest
    ): Response<SetHbtiResponse?>

    @POST("/recommend/trainer")
    suspend fun recommendTrainer(
        @Body recommendTrainerRequest: RecommendTrainerRequest
    ): Response<RecommendTrainerResponse?>

    @POST("/session/applysession")
    suspend fun applySession(
        @Body applySessionRequest: ApplySessionRequest
    ): Response<ApplySessionResponse?>


    @POST("/session/approvesession")
    suspend fun approveSession(
        @Body approveSessionRequest: ApproveSessionRequest
    ): Response<ApproveSessionResponse?>
    
    @POST("/session/disapprovesession")
    suspend fun disapproveSession(
        @Body approveSessionRequest: ApproveSessionRequest
    ): Response<ApproveSessionResponse?>

    @POST("/session/getTrainee")
    suspend fun getMyTraineeList(
        @Body myTraineesRequest: GetMyTraineesRequest
    ): Response<GetMyTraineesResponse?>

    @POST("/session/getTrainer")
    suspend fun getMyTrainerList(
        @Body myTrainersRequest: GetMyTrainersRequest
    ): Response<GetMyTrainersResponse?>

    @POST("/sns/createpost")
    suspend fun createPost(
        @Body createPostRequest: MultipartBody
    ): Response<CreatePostResponse?>

    @POST("/sns/editpost")
    suspend fun editPost(
        @Body editPostRequest: MultipartBody

    ): Response<CommonResponse?>

    @POST("/sns/deletepost")
    suspend fun deletePost(
        @Body deletePostRequest: DeletePostRequest
    ): Response<CommonResponse?>

    @POST("/sns/getPost")
    suspend fun getPost(
        @Body getPostRequest: GetPostRequest
    ): Response<GetPostResponse?>

    @POST("/sns/createComment")
    suspend fun createComment(
        @Body createCommentRequest: CreateCommentRequest
    ): Response<CreateCommentResponse?>

    @POST("/sns/editComment")
    suspend fun editComment(
        @Body editCommentRequest: EditCommentRequest
    ): Response<CommonResponse?>

    @POST("/sns/deleteComment")
    suspend fun deleteComment(
        @Body deleteCommentRequest: DeleteCommentRequest
    ): Response<CommonResponse?>

    @POST("/sns/getComment")
    suspend fun getComment(
        @Body getCommentRequest: GetCommentRequest
    ): Response<GetCommentResponse?>

    @POST("/Review/Create")
    suspend fun createReview(
        @Body createReviewRequest: CreateReviewRequest
    ): Response<CommonResponse?>

    @POST("/Review/Edit")
    suspend fun editReview(
        @Body editReviewRequest: EditReviewRequest
    ): Response<CommonResponse?>

    @POST("/Review/Delete")
    suspend fun deleteReview(
        @Body deleteReviewRequest: DeleteReviewRequest
    ): Response<CommonResponse?>

    @POST("/Review/get")
    suspend fun getReview(
        @Body getReviewRequest: GetReviewRequest
    ): Response<GetReviewResponse?>

}

// 해당 클래스는 싱글톤패턴 클래스로 getinstance로 가져와야함
object ApiManager {

    // 카페 데이터 baseURl
    private const val apiBaseUrl = "http://ec2-3-145-70-159.us-east-2.compute.amazonaws.com:3000"
//    private const val apiBaseUrl = "http://10.0.2.2:3000"


    private val apiclient = OkHttpClient.Builder().build()
    private var apiService: ApiService = Retrofit.Builder()
        .baseUrl(apiBaseUrl)
        .client(apiclient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    /**
     * ID중복확인 함수 단순 중복시 true리턴 중복아닐시 false리턴
     */
    suspend fun checkIdDupicated(id:String): Boolean= withContext(Dispatchers.IO){

        try {
            val response= apiService.checkIdDupicate(
                CheckIdRequest(id)
            )

            if(response.isSuccessful){
                Log.d("TAG","[check ID] 정상 작동 : checkid= ${id},result => ${response.body()?.isDuplicated}")

                val  result =response.body()?.isDuplicated
                return@withContext result!!

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
            return@withContext null
        }
        try {
            val response= apiService.registerUser(
                createRegisterReq(userdata)
            )

            if(response.isSuccessful){
                Log.d("TAG","[register] 정상 작동 : $userdata")

                val resultcode =response.body()?.code
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
            )
            if(response.isSuccessful){
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                val resultcode =response.body()?.code
                if(resultcode==200 &&response.body()?.userdata !=null){
                    val userdata = response.body()?.userdata!!
                    val subdata=response.body()?.subuserdata!!
                    val birthdate=LocalDate.parse(userdata.birth, formatter)
                    if(userdata.role=="1"){ // 트레이니
                        return@withContext TraineeData(userdata.name,userdata.login_id,userdata.password, userdata.gender,birthdate,true,userdata.usercategory,
                            subdata.description!!,subdata.trainee_id!!  ).apply {
                            this.uid=userdata.uid
                            this.registerDate=LocalDate.parse(userdata.registerDate,formatter)
                            this.hbti=userdata.hbti
                        }

                    }else{ //트레이너
                        return@withContext TrainerData(userdata.name,userdata.login_id,userdata.password, userdata.gender,birthdate,false,userdata.usercategory,
                            subdata.career!!,subdata.lesson!!,subdata.trainer_id!!,userdata.location, certificate = subdata.certificate ).apply {
                            this.uid=userdata.uid
                            this.registerDate=LocalDate.parse(userdata.registerDate,formatter)
                            this.hbti=userdata.hbti


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
            )
            if(response.isSuccessful){

                val resultcode =response.body()?.code
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


    /**
     * 비밀번호 찾기 이름, 아이디, 생년월일을 매개로 받아
     * 3개가 일치하는 계정 존재시  true , 아닐시 false
     */
    suspend fun checkFindPassword (id:String,name:String,birth: LocalDate): Boolean= withContext(Dispatchers.IO){

        try {
            val response= apiService.checkFindPassword(
                PasswordFindRequest(login_id = id, name =name, birth = birth.toString())
            )
            if(response.isSuccessful){

                val resultcode =response.body()?.code
                if(resultcode==200) {
                    return@withContext response.body()?.result!!
                }
                else{
                    Log.d("TAG","checkfindpassword ${response.body()?.message}")
                }

            }else{
                Log.d("TAG","checkfindpassword ${response.body()?.message}")
            }
        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
        }
        false
    }
    /**
     * 비밀번호 변경 이름, 아이디, 생년월일, 바꿀 비밀번호 을 매개로 받아
     * 3개가 일치하는 계정 존재시  true , 아닐시 false
     */
    suspend fun changeFindPassword (id:String,name:String,birth: LocalDate,newpassword: String): Boolean= withContext(Dispatchers.IO){

        try {
            val response= apiService.changeFindPassword(
                PasswordFindRequest(login_id = id, name =name, birth = birth.toString(), newpassword = newpassword)
            )
            if(response.isSuccessful){

                val resultcode =response.body()?.code
                if(resultcode==200) {
                    return@withContext response.body()?.result!!
                }
                else{
                    Log.d("TAG","changeFindPassword ${response.body()?.message}")
                }

            }else{
                Log.d("TAG","changeFindPassword ${response.body()?.message}")
            }
        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
        }
        false
    }

    /**
     * 트레이너 서치  category,gender( null 가능-> 남녀 상관x), location( string 단순 포함)  을 매개로 받아
     * TrainerProfile 리스트 리턴
     */
    suspend fun searchTrainer (category:String,gender:String?,location: String): List<TrainerProfile>? = withContext(Dispatchers.IO){
        try {
            val response= apiService.serachTrainer(
                TrainerSearchRequest(category,gender, location)
            )
            if(response.isSuccessful){

                val resultcode =response.body()?.code
                if(resultcode==200) {
                    return@withContext response.body()!!.trainerlist
                }
                else{
                    Log.d("TAG","searchTrainer ${response.body()?.message}")
                }
            }else{
                Log.d("TAG","searchTrainer ${response.body()?.message}")
            }
        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
        }
        null
    }



    /**
     * gethbti 유저의 uid를 매개로 받아 해당 유저의 hbti리턴  리스트형태 int
     * hbti는 Int 5개의 리스트 -> 각 값 0~100
     */
    suspend fun getHbti (uid :Int): List<Int>? = withContext(Dispatchers.IO){
        try {
            val response= apiService.getHbti(
                GetHbtiRequest(uid)
            )
            if(response.isSuccessful){

                val resultcode =response.body()?.code
                if(resultcode==200) {
                    return@withContext response.body()!!.hbti
                }
                else{
                    Log.d("TAG","getHbti ${response.body()?.message}")
                    return@withContext null
                }
            }else{
                Log.d("TAG","getHbti ${response.body()?.message}")
            }
        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
        }
        null
    }


    /**
     * sethbti 유저의 uid,int리스트형태  hbti 를 매개로 받아 성공실패 여부 리턴
     * hbti는 Int 5개의 리스트 -> 각 값 0~100
     */
    suspend fun setHbti (uid :Int,hbti:List<Int>): Boolean = withContext(Dispatchers.IO){
        try {
            val response= apiService.setHbti(
                SetHbtiRequest(uid,hbti)
            )
            if(response.isSuccessful){

                val resultcode =response.body()?.code
                if(resultcode==200) {
                    return@withContext true
                }
                else{
                    Log.d("TAG","setHbti ${response.body()?.message}")
                    return@withContext false
                }
            }else{
                Log.d("TAG","setHbti ${response.body()?.message}")
            }
        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
        }
        false
    }

    /**
     * 트레이너 추천 category,gender( null 가능-> 남녀 상관x), location(""-> 상관없음), int리스트형태  hbti  을 매개로 받아
     * TrainerProfile 리스트 리턴 ,
     * hbti는 Int 5개의 리스트 -> 각 값 0~100
     */
    suspend fun recommendTrainer (category:String="000000",gender:String?=null,location: String="",hbti:List<Int>): List<TrainerProfile>? = withContext(Dispatchers.IO){
        try {
            val response= apiService.recommendTrainer(
                RecommendTrainerRequest(category,gender, location,hbti)
            )
            if(response.isSuccessful){

                val resultcode =response.body()?.code
                if(resultcode==200) {
                    return@withContext response.body()!!.trainerlist
                }
                else{
                    Log.d("TAG","recommendTrainer ${response.body()?.message}")
                }
            }else{
                Log.d("TAG","recommendTrainer ${response.body()?.message}")
            }
        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
        }
        null
    }

    /**
     * 매칭 신청 applysession 트레이니가 트레이너 한테 매칭 신청 -> 신청일 뿐 바로 매칭 x  , 트레이너가 승인해야함 ->매칭
     * traineeUid, trainerUid ( 둘다 Userdata의 uid UUUUid를 넣어야함)  를 매개로 받아 신청 ,오류 여부 리턴
     */
    suspend fun applySession (traineeUid :Int,trainerUid :Int): Boolean = withContext(Dispatchers.IO){
        try {
            val response= apiService.applySession(
                ApplySessionRequest(traineeUid,trainerUid)
            )
            if(response.isSuccessful){

                val resultcode =response.body()?.code
                if(resultcode==200) {
                    return@withContext true
                }
                else{
                    Log.d("TAG","applySession ${response.body()?.message}")
                    return@withContext false
                }
            }else{
                Log.d("TAG","applySession ${response.body()?.message}")
            }
        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
        }
        false
    }

    /**
     * 매칭 승인 approveSession 트레이너가 getmytrainee list에서 가져온 신청자를  승인
     * trainerUid(Userdata의 uid를 넣어야함) , sid (-> getMySession에서 가져올 수있음)를 매개로 받아  승인 ,오류 여부 리턴
     * 매칭 승인이 아닌 거절을 하고싶으면 disapprove를 true로 -> 신청중인 거 뿐 아니라 존재하는 승인된 매칭 삭제도 됨
     */
    suspend fun approveSession (trainerUid :Int,sid :Int,disapprove:Boolean=false): Boolean = withContext(Dispatchers.IO){
        try {
            val response= if(!disapprove) apiService.approveSession(
                ApproveSessionRequest(trainerUid,sid)
            ) else apiService.disapproveSession(
                ApproveSessionRequest(trainerUid,sid)
            )
            if(response.isSuccessful){

                val resultcode =response.body()?.code
                if(resultcode==200) {
                    return@withContext true
                }
                else{
                    Log.d("TAG","approveSession ${response.body()?.message}")
                    return@withContext false
                }
            }else{
                Log.d("TAG","approveSession ${response.body()?.message}")
            }
        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
        }
        false
    }


    /**
     * 내 세션 가져오기 getMySession 트레이너 혹은 트레이니가 자신의 현재 세션(매칭  트레이니가 신청상태인(sessionNow=0), 트레이너가 승인한(sessionNow=1) )
     * 본인이 트레이너인지 여부와 자신의 uid  를 매개로 받아 현재 자신과 연관된 세션 리스트 리턴 ,null리턴 오류
     */
    suspend fun getMySession(amITrainer: Boolean ,myUid:Int): List<MySession>? = withContext(Dispatchers.IO){
        try {
            if(amITrainer){
                val response= apiService.getMyTraineeList(
                    GetMyTraineesRequest(myUid)
                )
                if(response.isSuccessful){
                    val resultcode =response.body()?.code
                    if(resultcode==200) {
                        return@withContext response.body()?.toList()
                    }
                    else{
                        Log.d("TAG","getMySession ${response.body()?.message}")
                        return@withContext null
                    }
                }else{
                    Log.d("TAG","getMySession ${response.body()?.message}")
                }
            }else{
                val response= apiService.getMyTrainerList(
                    GetMyTrainersRequest(myUid)
                )
                if(response.isSuccessful){
                    val resultcode =response.body()?.code
                    if(resultcode==200) {
                        return@withContext response.body()?.toList()
                    }
                    else{
                        Log.d("TAG","getMySession ${response.body()?.message}")
                        return@withContext null
                    }
                }else{
                    Log.d("TAG","getMySession ${response.body()?.message}")
                }
            }


        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
        }
        null
    }

    /**
     * 트레이너 프로필 가져오기 -트레이너 서치의 리스트의 객체와 같은 클래스 리턴
     *
     * 트레이너의 uid를 매개로 하여
     *
     * 트레이너 프로필을 리턴, 오류 null리턴
     */
    suspend fun getTrainerProfile (trainerUid:Int): TrainerProfile? = withContext(Dispatchers.IO){
        try {
            val response= apiService.getTrainerProfile(
                GetTrainerProfileRequest(trainerUid)
            )
            if(response.isSuccessful){

                val resultcode =response.body()?.code
                if(resultcode==200) {
                    return@withContext  response.body()!!.trainerProfile
                }
                else{
                    Log.d("TAG","getTrainerProfile ${response.body()?.message}")
                    return@withContext null
                }
            }else{
                Log.d("TAG","getTrainerProfile ${response.body()?.message}")
            }
        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
        }
        null
    }


    // SNS
    // 게시글 api

    /**
     * createPost 유저가 글 작성 -
     *유저의 uid, 제목, 내용, 글의 카테고리(입력안하면=000000 카테고리 없음) 를 매개
     *
     * 글이 작성되면 작성 된 글의 pid 리턴, 작성 오류시 null 리턴
     */
    suspend fun createPost (uid:Int,title:String,content:String,category: String="000000",imageFile: File?=null): Int? = withContext(Dispatchers.IO){
        try {

            var tempbody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid",uid.toString())
                .addFormDataPart("title",title)
                .addFormDataPart("content",content)
                .addFormDataPart("category",category)
            if(imageFile!=null){
                tempbody.addFormDataPart("image",imageFile.name,
                    imageFile.asRequestBody("image/jpeg".toMediaType())
                )
            }
            var body=tempbody.build()


//

            val response= apiService.createPost(
                body
            )



            if(response.isSuccessful){

                val resultcode =response.body()?.code
                if(resultcode==200) {
                    return@withContext  response.body()!!.postid
                }
                else{
                    Log.d("TAG","createPost ${response.body()?.message}")
                    return@withContext null
                }
            }else{
                Log.d("TAG","createPost ${response.body()?.message}")
            }
        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
        }
        null
    }

    /**
     * 글 삭제 deletePost  유저가 글 삭제
     *
     * 유저의 uid, 글의 pid 를 매개
     *
     * 글이 삭제되면 true, 삭제된게 없을시 false
     */
    suspend fun deletePost (uid:Int,pid:Int): Boolean = withContext(Dispatchers.IO){
        try {
            val response= apiService.deletePost(
                DeletePostRequest(uid, pid)
            )
            if(response.isSuccessful){

                val resultcode =response.body()?.code
                if(resultcode==200) {
                    return@withContext true
                }
                else{
                    Log.d("TAG","createPost ${response.body()?.message}")
                    return@withContext false
                }
            }else{
                Log.d("TAG","createPost ${response.body()?.message}")
            }
        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
        }
        false
    }

    /**
     * 글 수정 editPost  유저가 글 수정
     *
     * 유저의 uid, 원게시글의 pid ,제목, 내용, 글의 카테고리(입력안하면=000000 카테고리 없음) 를 매개변수로
     * isimagechange는 기존의 이미지를 수정할지 안할지 true/false  ex) true하고 imagefile을 null로하면 글 이미지 삭제
     *
     *   글이 수정되면 true리턴 , 수정 오류시 false
     */
    suspend fun editPost (uid:Int, pid: Int,title:String,content:String,category: String="000000",isimagechange:Boolean=false,imageFile: File?=null): Boolean = withContext(Dispatchers.IO){
        try {
            var tempbody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid",uid.toString())
                .addFormDataPart("pid",pid.toString())
                .addFormDataPart("title",title)
                .addFormDataPart("content",content)
                .addFormDataPart("category",category)
            if(isimagechange){
                tempbody.addFormDataPart("isImagechange","true");
            }

            if(isimagechange&& imageFile!=null){
                tempbody.addFormDataPart("image",imageFile.name,
                    imageFile.asRequestBody("image/jpeg".toMediaType())
                )
            }
            var body=tempbody.build()

            val response= apiService.editPost(
                body
            )
            if(response.isSuccessful){

                val resultcode =response.body()?.code
                if(resultcode==200) {
                    return@withContext true
                }
                else{
                    Log.d("TAG","editPost ${response.body()?.message}")
                    return@withContext false
                }
            }else{
                Log.d("TAG","editPost ${response.body()?.message}")
            }
        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
        }
        false
    }

    /**
     * 글 리스트 get - 카테고리로도, 작성자 uid로도 선택
     *
     * 유저 uid(null 로 하면 모든 사용자의 글 출력 ), 카테고리(입력안하면=000000 카테고리 없음) 매개변수
     *
     * 글 리스트 리턴 오류시 null 리턴
     */
    suspend fun getPost (uid:Int?,category: String="000000"): List<Postdata>? = withContext(Dispatchers.IO){
        try {
            val response= apiService.getPost(
                GetPostRequest(uid,category)
            )
            if(response.isSuccessful){

                val resultcode =response.body()?.code
                if(resultcode==200) {
                    return@withContext  response.body()!!.postlist
                }
                else{
                    Log.d("TAG","getPost ${response.body()?.message}")
                    return@withContext null
                }
            }else{
                Log.d("TAG","getPost ${response.body()?.message}")
            }
        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
        }
        null
    }


    // 댓글 api

    /**
     * createComment 유저가 게시글의 댓글 작성 -
     *
     *유저의 uid, 게시글의 pid ,내용 를 매개
     *
     * 댓글이 작성되면 작성 된 댓글의 cid 리턴, 작성 오류시 null 리턴
     */
    suspend fun createComment (uid:Int,pid:Int,content:String): Int? = withContext(Dispatchers.IO){
        try {
            val response= apiService.createComment(
                CreateCommentRequest(uid, pid,content)
            )
            if(response.isSuccessful){

                val resultcode =response.body()?.code
                if(resultcode==200) {
                    return@withContext  response.body()!!.commentid
                }
                else{
                    Log.d("TAG","createComment ${response.body()?.message}")
                    return@withContext null
                }
            }else{
                Log.d("TAG","createComment ${response.body()?.message}")
            }
        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
        }
        null
    }

    /**
     * 댓글 삭제 deleteComment  게시글의 댓글 삭제
     *
     * 유저의 uid, 댓글의 cid 를 매개
     *
     * 댓글이 삭제되면 true, 삭제된게 없을시 false
     */
    suspend fun deleteComment (uid:Int,cid:Int): Boolean = withContext(Dispatchers.IO){
        try {
            val response= apiService.deleteComment(
                DeleteCommentRequest(uid, cid)
            )
            if(response.isSuccessful){

                val resultcode =response.body()?.code
                if(resultcode==200) {
                    return@withContext true
                }
                else{
                    Log.d("TAG","deleteComment ${response.body()?.message}")
                    return@withContext false
                }
            }else{
                Log.d("TAG","deleteComment ${response.body()?.message}")
            }
        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
        }
        false
    }

    /**
     * 댓글 수정 editComment  유저가 글 수정
     *
     * 작성유저의 uid, 수정될 댓글의 cid , 내용을 매개변수로
     *
     *   댓글이 수정되면 true리턴 , 수정 오류시 false
     */
    suspend fun editComment (uid:Int, cid: Int,content:String): Boolean = withContext(Dispatchers.IO){
        try {
            val response= apiService.editComment(
                EditCommentRequest(uid, cid,content)
            )
            if(response.isSuccessful){

                val resultcode =response.body()?.code
                if(resultcode==200) {
                    return@withContext true
                }
                else{
                    Log.d("TAG","editComment ${response.body()?.message}")
                    return@withContext false
                }
            }else{
                Log.d("TAG","editComment ${response.body()?.message}")
            }
        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
        }
        false
    }

    /**
     * 댓글 리스트 get 해당 게시물의 댓글을 가져온다
     *
     * 게시글의 pid 를 매개변수
     *
     * 댓글 리스트 리턴 오류시 null 리턴
     */
    suspend fun getComment (pid :Int): List<Comment>? = withContext(Dispatchers.IO){
        try {
            val response= apiService.getComment(
                GetCommentRequest(pid)
            )
            if(response.isSuccessful){

                val resultcode =response.body()?.code
                if(resultcode==200) {
                    return@withContext  response.body()!!.comments
                }
                else{
                    Log.d("TAG","getPost ${response.body()?.message}")
                    return@withContext null
                }
            }else{
                Log.d("TAG","getPost ${response.body()?.message}")
            }
        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
        }
        null
    }



    // 리뷰 api

    /**
     * createReview 트레이니가 트레이너의 리뷰 작성 - 아마 session의 존재여부 확인하고 함수실항하는식으로 해야할듯
     *
     * 트레이너uid, 트레이니 uid, 별점 (0.0~ 10.0) db자료구조가 demical(3,1)로 되어잇음 ,내용 를 매개
     *
     * 리뷰 작성되면 true 리턴 , 작성 오류시 false 리턴 -> 리뷰는 따로 pk id가 없으며, traineruid,traineeuid가 pk이다
     */
    suspend fun createReview (traineruid:Int,traineeuid:Int,score:Double,content:String): Boolean = withContext(Dispatchers.IO){
        if(score<0.0 || score>10.0 ){
            Log.d("TAG","createReview score은 0<= score<=10 을 만족해야합니다.")
            return@withContext false;
        }
        try {
            val response= apiService.createReview(
                CreateReviewRequest(traineruid, traineeuid,content,score)
            )
            if(response.isSuccessful){

                val resultcode =response.body()?.code
                if(resultcode==200) {
                    return@withContext  true;
                }
                else{
                    Log.d("TAG","createReview ${response.body()?.message}")
                    return@withContext false
                }
            }else{
                Log.d("TAG","createReview ${response.body()?.message}")
            }
        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
        }
        false
    }

    /**
     * deleteReview 작성된 리뷰 삭제
     *
     * 트레이너uid, 트레이니 uid 를 매개 -> 리뷰는 따로 pk id가 없으며, traineruid,traineeuid가 pk이다
     *
     * 리뷰 삭제되면 true 리턴 , 삭제 오류시 false 리턴
     */
    suspend fun deleteReview (traineruid:Int,traineeuid:Int): Boolean = withContext(Dispatchers.IO){

        try {
            val response= apiService.deleteReview(
                deleteReviewRequest = DeleteReviewRequest(traineruid, traineeuid)
            )
            if(response.isSuccessful){

                val resultcode =response.body()?.code
                if(resultcode==200) {
                    return@withContext  true;
                }
                else{
                    Log.d("TAG","deleteReview ${response.body()?.message}")
                    return@withContext false
                }
            }else{
                Log.d("TAG","deleteReview ${response.body()?.message}")
            }
        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
        }
        false
    }

    /**
     * createReview 트레이니가 트레이너의 리뷰 작성 - 아마 session의 존재여부 확인하고 함수실항하는식으로 해야할듯
     *
     * 트레이너uid, 트레이니 uid, 별점 (0.0~ 10.0) db자료구조가 demical(3,1)로 되어잇음 ,내용 를 매개
     *
     * 리뷰 작성되면 true 리턴 , 작성 오류시 false 리턴 -> 리뷰는 따로 pk id가 없으며, traineruid,traineeuid가 pk이다
     */
    suspend fun editReview (traineruid:Int,traineeuid:Int,score:Double,content:String): Boolean = withContext(Dispatchers.IO){
        if(score<0.0 || score>10.0 ){
            Log.d("TAG","editReview score은 0<= score<=10 을 만족해야합니다.")
            return@withContext false;
        }
        try {
            val response= apiService.editReview(
                EditReviewRequest(traineruid, traineeuid,content,score)
            )
            if(response.isSuccessful){

                val resultcode =response.body()?.code
                if(resultcode==200) {
                    return@withContext  true;
                }
                else{
                    Log.d("TAG","editReview ${response.body()?.message}")
                    return@withContext false
                }
            }else{
                Log.d("TAG","editReview ${response.body()?.message}")
            }
        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
        }
        false
    }

    /**
     * getReview 리뷰 리스트 해당 트레이너의 리뷰를 가져온다
     *
     * traineruid를 매개로 받는다.
     *
     * 리뷰 리스트 리턴 오류시 null 리턴
     */
    suspend fun getReview (traineruid :Int): List<Review>? = withContext(Dispatchers.IO){
        try {
            val response= apiService.getReview(
                GetReviewRequest(traineruid)
            )
            if(response.isSuccessful){

                val resultcode =response.body()?.code
                if(resultcode==200) {
                    return@withContext  response.body()!!.reviews
                }
                else{
                    Log.d("TAG","getReview ${response.body()?.message}")
                    return@withContext null
                }
            }else{
                Log.d("TAG","getReview ${response.body()?.message}")
            }
        }catch (e :Exception){
            Log.d("TAG","error 발생 :--------${e}")
        }
        null
    }

    /**
     * getSnsImage 해당 게시글 이미지 가져오기
     *
     * pid를 매개로 받는다.
     *
     * 이미지가 있는지 없는지 check 미구현 단순 url출력
     */
    fun getSnsImage(pid: Int) :String{
        return "$apiBaseUrl/snsimage/${pid}"
    }



}


