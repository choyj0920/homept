// main.js
var mysql = require("mysql");
var express = require("express");
var bodyParser = require("body-parser");
var config = require("./db_info").maindb;
var app = express();
const crypto = require("crypto");

const options = { timeZone: "Asia/Seoul" };

const key = "Zolfproject6team"; // 키
const iv = "6teamZolfproject"; // 초기화 벡터

// 복호화 함수
function decryptAES128(cipherText) {
  const decipher = crypto.createDecipheriv("aes-128-cbc", key, iv);
  let decrypted = decipher.update(cipherText, "base64", "utf8");
  decrypted += decipher.final("utf8");
  return decrypted;
}
function encryptAES128(plainText) {
  const cipher = crypto.createCipheriv("aes-128-cbc", key, iv);
  let encrypted = cipher.update(plainText, "utf8", "base64");
  encrypted += cipher.final("base64");
  return encrypted;
}

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

//db 연결
var connection = mysql.createConnection({
  host: config.host,
  user: config.user,
  database: config.database,
  password: config.password,
  port: config.port,
  timezone: config.timezone,
});

try {
  var interval = setInterval(function () {
    console.log(`${new Date().toLocaleString("ko-kr")} : db커넥션 유지`);
    connection.query("SELECT 1");
  }, 3600000);
  app.listen(3000, "0.0.0.0", function () {
    console.log(`${new Date().toLocaleString("ko-kr")} : 서버 실행 중...`);
  });
} catch (error) {
  clearInterval(interval);

  exit(-1);
}

// 유저 아이디 중복체크
app.post("/user/checkId", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [checkID]`);
  console.log(req.body);

  var id = req.body.id;

  // 유저 아이디 중복 체크를 위한 SQL 쿼리와 파라미터
  var sql = "SELECT * FROM user WHERE login_id = ?;";
  var params = [id];

  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";
    var isDuplicated = false;

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "오류 발생";
    } else {
      if (result.length > 0) {
        // 결과가 존재하면 아이디가 중복됨
        isDuplicated = true;
        resultCode = 200;
        message = "중복된 아이디입니다";
      } else {
        // 결과가 없으면 아이디 사용 가능
        resultCode = 200;
        message = "사용 가능한 아이디입니다";
      }
    }

    res.json({
      code: resultCode,
      message: message,
      isDuplicated: isDuplicated,
    });
  });
});

// 유저 회원가입
app.post("/user/register", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [register ]`);

  // dereq=decryptAES128(req);
  dereq = req;

  console.log(dereq.body);

  var name = dereq.body.name;
  var id = dereq.body.id;
  var password = dereq.body.password;
  var gender = dereq.body.gender;
  var birth = dereq.body.birth;
  var isTrainee = dereq.body.isTrainee;
  var usercategory = dereq.body.usercategory;

  var sql =
    "INSERT INTO user (login_id,name,password,gender,birth,role,usercategory,register_date,login_date) VALUES (?,?,?,?,?,?,?,now(),now());";
  var params = [
    id,
    name,
    password,
    gender,
    birth,
    isTrainee ? "1" : "0",
    usercategory,
  ];

  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "유저 데이터 삽입중 오류 발생";

      res.json({
        code: resultCode,
        message: message,
      });
    } else {
      message = "유저 데이터 삽입 완료";
      var uid = result.insertId;

      if (isTrainee) {
        subSql = "INSERT INTO trainee (uid,description) VALUES (?,?)";
        subparams = [uid, dereq.body.description];
      } else {
        subSql = "INSERT INTO trainer (uid,career,lesson) VALUES (?,?,?)";
        subparams = [uid, dereq.body.career, dereq.body.lesson];
      }
      connection.query(subSql, subparams, function (err, result) {
        var resultCode = 404;
        message = "에러가 발생했습니다";
        var subuid = null;

        if (err) {
          console.log(err);
          resultCode = 400;
          message = message += `${
            isTrainee ? "트레이니" : "트레이너"
          } 데이터 삽입 오류`;
        } else {
          resultCode = 200;
          message = `${isTrainee ? "트레이니" : "트레이너"} 데이터 삽입 완료`;
          subuid = result.insertId;
        }

        res.json({
          code: resultCode,
          message: message,
          uid: uid,
          subuid: subuid,
        });
      });
    }
  });
});

function changeDatetoKoreaStr(date) {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  const formattedDate = `${year}-${month}-${day}`;
  return formattedDate;
}

app.post("/user/login", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [login]`);

  console.log(req.body);
  // dereq=decryptAES128(req);
  dereq = req;

  var id = dereq.body.login_id;
  var password = dereq.body.password;

  // 해당 유저 id,password확인
  var sql = "select * FROM user WHERE login_id = ? and password = ?;";
  var params = [id, password];

  var updateLogin =
    "UPDATE user SET login_date = NOW() WHERE login_id=? and password = ?;";

  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";
    var userdata = null;

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "로그인 에러 발생";
    } else {
      if (result.length > 0) {
        resultCode = 200;
        message = "로그인 성공";
        var user = result[0];

        userdata = {
          uid: user.uid,
          login_id: user.login_id,
          name: user.name,
          password: user.password,
          gender: user.gender,
          birth: changeDatetoKoreaStr(user.birth),
          role: user.role,
          usercategory: user.usercategory,
          register_date: changeDatetoKoreaStr(user.register_date),
          login_date: changeDatetoKoreaStr(user.login_date),
        };
        var uid = userdata.uid;

        //트레이니
        subsql = `select * from ${
          userdata.role == "0" ? "trainer" : "trainee"
        } where uid= ?`;
        subparams = [uid];

        connection.query(subsql, subparams, function (err, subresult) {
          var resultCode = 404;
          var message = "에러가 발생했습니다";
          var subUserdata = null;

          if (err) {
            console.log(err);
            resultCode = 400;
            message = "로그인 서브데이터 로드 오류 발생";
          } else {
            if (subresult.length > 0) {
              resultCode = 200;
              message = "로그인 성공";
              connection.query(
                updateLogin,
                params,
                function (err, loginresult) {}
              );

              subUserdata = subresult[0];
            } else {
              console.log(err);
              resultCode = 401;
              message = "로그인 서브데이터 로드 실패";
            }
          }
          res.json({
            code: resultCode,
            message: message,
            userdata: userdata,
            subuserdata: subUserdata,
          });
        });
      } else {
        resultCode = 401;
        message = "아이디 또는 비밀번호가 잘못 되었습니다.";

        res.json({
          code: resultCode,
          message: message,
          userdata: null,
        });
      }
    }
  });
});

app.post("/user/unregister", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [탈퇴...]`);
  console.log(req.body);
  // dereq=decryptAES128(req);
  dereq = req;

  var id = dereq.body.id;
  var password = dereq.body.password;

  // 탈퇴 코드
  var sql = "delete FROM user where login_id = ? AND password= ?;";
  var params = [id, password];

  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";
    var isDeleted = false;

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "오류 발생";
    } else if (result.affectedRows > 0) {
      // 영향을 받은 행의 개수가 1 이상일 경우 삭제 성공으로 간주
      resultCode = 200;
      message = "회원 탈퇴가 완료되었습니다";
      isDeleted = true;
    } else {
      // 영향을 받은 행이 없을 경우 삭제 실패로 간주
      resultCode = 404;
      message = "회원 탈퇴에 실패했습니다. 아이디와 비밀번호를 확인해주세요";
    }

    res.json({
      code: resultCode,
      message: message,
      isDeleted: isDeleted,
    });
  });
});
