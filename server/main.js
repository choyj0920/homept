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
  var location = dereq.body.location;

  var sql =
    "INSERT INTO user (login_id,name,password,gender,birth,role,usercategory,register_date,login_date,location) VALUES (?,?,?,?,?,?,b?,now(),now(),?);";
  var params = [
    id,
    name,
    password,
    gender,
    birth,
    isTrainee ? "1" : "0",
    usercategory,
    location,
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

function bitToString(bit, slicecnt) {
  const binaryString = bit
    .toString("hex")
    .split("")
    .map((hexChar) => parseInt(hexChar, 16).toString(2).padStart(4, "0"))
    .join("");

  return binaryString.slice(slicecnt);
}

// 유저 hbti get
app.post("/user/gethbti", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [get Hbti]`);
  console.log(req.body);

  var uid = req.body.uid;

  // 유저 hbti get 쿼리
  var sql = "select hbti1,hbti2,hbti3,hbti4,hbti5 from user where uid= ? ;";
  var params = [uid];

  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";
    var hbti = null;

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "오류 발생";
    } else {
      if (result.length > 0) {
        resultCode = 200;
        message = "hbti get 성공";
        var user = result[0];
        hbti =
          user.hbti1 != null
            ? [user.hbti1, user.hbti2, user.hbti3, user.hbti4, user.hbti5]
            : null;
      } else {
        resultCode = 204;
        message = "해당되는 uid 유저를 찾지 못했습니다";
      }
    }

    res.json({
      code: resultCode,
      message: message,
      hbti: hbti,
    });
  });
});

// 유저 hbti set
app.post("/user/sethbti", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [set Hbti]`);
  console.log(req.body);

  var uid = req.body.uid;
  var hbti = req.body.hbti;

  // 유저 hbti set 쿼리
  var sql =
    "update user set hbti1=?, hbti2=?, hbti3=?, hbti4=?, hbti5=? where uid =?;";
  var params = hbti.concat([uid]);
  console.log(params);

  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "오류 발생";
    } else {
      if (result.affectedRows > 0) {
        resultCode = 200;
        message = "hbti set 성공";
      } else {
        resultCode = 204;
        message = "해당되는 uid 유저를 찾지 못했습니다";
      }
    }

    res.json({
      code: resultCode,
      message: message,
    });
  });
});

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

        // console.log(user.usercategory);
        // console.log(typeof user.usercategory);

        userdata = {
          uid: user.uid,
          login_id: user.login_id,
          name: user.name,
          password: user.password,
          gender: user.gender,
          birth: changeDatetoKoreaStr(user.birth),
          role: user.role,
          // 현재 category는 6개 밖에 없으나 db의 자료구조가 BIT(8)이어서 앞에 2개를 자르는 처리가 필요
          usercategory: bitToString(user.usercategory, 2),
          location: user.location,
          register_date: changeDatetoKoreaStr(user.register_date),
          login_date: changeDatetoKoreaStr(user.login_date),
          hbti:
            user.hbti1 != null
              ? [user.hbti1, user.hbti2, user.hbti3, user.hbti4, user.hbti5]
              : null,
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

app.post("/user/findpassword/check", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [비밀번호찾기 체크...]`);
  console.log(req.body);
  // dereq=decryptAES128(req);
  dereq = req;

  var id = dereq.body.login_id;
  var name = dereq.body.name;
  var birth = dereq.body.birth;

  // 비밀번호 찾기 쿼리
  var sql = "select * FROM user WHERE login_id = ? and name = ? and birth = ?;";
  var params = [id, name, birth];

  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";
    var isfinded = false;

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "오류 발생";
    } else if (result.length > 0) {
      // 영향을 받은 행의 개수가 1 이상일 경우 삭제 성공으로 간주
      resultCode = 200;
      message = "해당하는 계정이 존재합니다.";
      isfinded = true;
    } else {
      // 영향을 받은 행이 없을 경우 삭제 실패로 간주
      resultCode = 401;
      message = "해당하는 계정이 존재하지 않습니다.";
    }

    res.json({
      code: resultCode,
      message: message,
      result: isfinded,
    });
  });
});
app.post("/user/findpassword/change", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [비밀번호찾기 변경...]`);
  console.log(req.body);
  // dereq=decryptAES128(req);
  dereq = req;

  var id = dereq.body.login_id;
  var name = dereq.body.name;
  var birth = dereq.body.birth;
  var newpassword = dereq.body.newpassword;

  // 비밀번호 찾기 쿼리
  var sql =
    "update user set password=? WHERE login_id = ? and name = ? and birth = ?;";
  var params = [newpassword, id, name, birth];

  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";
    var ischanged = false;

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "오류 발생";
    } else if (result.affectedRows > 0) {
      resultCode = 200;
      message = "비밀번호 변경 완료";
      ischanged = true;
    } else {
      // 영향을 받은 행이 없을 경우 삭제 실패로 간주
      resultCode = 401;
      message = "해당하는 계정이 존재하지 않습니다.";
    }

    res.json({
      code: resultCode,
      message: message,
      result: ischanged,
    });
  });
});

app.post("/search/trainer", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [트레이너 서치...]`);
  console.log(req.body);
  // dereq=decryptAES128(req);
  dereq = req;

  var category = dereq.body.category;
  var gender = dereq.body.gender;
  var location = dereq.body.location;

  // 트레이너 찾기 쿼리
  var sql =
    'select t.uid,trainer_id,name,gender,career,certificate,lesson,LPAD(BIN(usercategory),6,"0") as usercategory ,location, hbti1,hbti2,hbti3,hbti4,hbti5 from trainer as t inner join user as u on t.uid=u.uid where (usercategory & b?)=b? and location LIKE ?' +
    (gender != null ? "and gender=?;" : ";");
  var params = [category, category, "%" + location + "%"];
  if (gender != null) {
    params.push(gender);
  }

  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";
    var trainerlist = null;

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "오류 발생";
    } else {
      resultCode = 200;
      message = "해당하는 트레이너 리스트 리턴";
      trainerlist = changeHbtiList(result);
    }

    res.json({
      code: resultCode,
      message: message,
      trainerlist: trainerlist,
    });
  });
});

// 트레이너 추천
app.post("/recommend/trainer", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [트레이너 추천...]`);
  console.log(req.body);
  // dereq=decryptAES128(req);
  dereq = req;

  var category = dereq.body.category;
  var gender = dereq.body.gender;
  var location = dereq.body.location;

  if (req.body.hbti == null) {
    res.json({
      code: 404,
      message: "hbti is null",
    });
    return;
  }
  var hbti = req.body.hbti;

  // 트레이너 추천 쿼리
  var sql =
    'select t.uid,trainer_id,name,gender,career,certificate,lesson,LPAD(BIN(usercategory),6,"0") as usercategory ,location, hbti1,hbti2,hbti3,hbti4,hbti5 ,' +
    " 100-(ABS(hbti1 - ?) + ABS(hbti2 - ?) + ABS(hbti3 - ?) + ABS(hbti4 - ?) + ABS(hbti5 - ?)) div 5 as matchingscore " +
    "from trainer as t inner join user as u on t.uid=u.uid where (usercategory & b?)=b? and location LIKE ?" +
    (gender != null ? "and gender=?" : "") +
    "and u.hbti1 is not null order by matchingscore desc;";
  var params = [
    hbti[0],
    hbti[1],
    hbti[2],
    hbti[3],
    hbti[4],
    category,
    category,
    "%" + location + "%",
  ];
  if (gender != null) {
    params.push(gender);
  }

  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";
    var trainerlist = null;

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "오류 발생";
    } else {
      resultCode = 200;
      message = "해당하는 트레이너 리스트 리턴";

      trainerlist = changeHbtiList(result);
    }

    res.json({
      code: resultCode,
      message: message,
      trainerlist: trainerlist,
    });
  });
});

// 매칭 신청
app.post("/session/applysession", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [register ]`);

  // dereq=decryptAES128(req);
  dereq = req;

  console.log(dereq.body);

  var traineeId = dereq.body.traineeUid;
  var trainerId = dereq.body.trainerUid;
  var trainee_memo = dereq.body.trainee_memo;

  var sql =
    "INSERT INTO TrainSession (trainee_id,trainer_id,sessionnow,trainee_memo) VALUES (?,?,0,?);";
  var params = [traineeId, trainerId, trainee_memo != null ? trainee_memo : ""];

  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";
    var isSucess = false;

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "매칭 신청 오류 발생";
    } else {
      if (result.insertId >= 0) {
        resultCode = 200;
        message = "매칭 신청 삽입 완료";
        isSucess = true;
      }
    }

    res.json({
      code: resultCode,
      message: message,
      isSucess: isSucess,
    });
  });
});

// 매칭 승인
app.post("/session/approvesession", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [approve ]`);

  // dereq=decryptAES128(req);
  dereq = req;

  console.log(dereq.body);

  var trainerId = dereq.body.trainerUid;
  var sid = dereq.body.sid;

  var sql =
    "Update TrainSession set sessionnow=1 where sid=? and trainer_id = ?;";
  var params = [sid, trainerId];

  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";
    var isSucess = false;

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "매칭 승인 오류 발생";
    } else {
      if (result.affectedRows > 0) {
        resultCode = 200;
        message = "매칭 신청 승인 완료";
        isSucess = true;
      }
    }

    res.json({
      code: resultCode,
      message: message,
      isSucess: isSucess,
    });
  });
});
// 매칭 거절
app.post("/session/disapprovesession", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [disapprove ]`);

  // dereq=decryptAES128(req);
  dereq = req;

  console.log(dereq.body);

  var trainerId = dereq.body.trainerUid;
  var sid = dereq.body.sid;

  var sql = "delete from TrainSession where sid=? and trainer_id = ?;";
  var params = [sid, trainerId];

  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";
    var isSucess = false;

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "매칭 거절 오류 발생";
    } else {
      if (result.affectedRows > 0) {
        resultCode = 200;
        message = "매칭 거절 승인 완료";
        isSucess = true;
      }
    }

    res.json({
      code: resultCode,
      message: message,
      isSucess: isSucess,
    });
  });
});

// 리스트 hbti make list
function changeHbtiList(result) {
  if (result.length < 1) {
    return result;
  }
  result.forEach((user) => {
    if (user["hbti1"] == null) {
      user.hbti = null;
    } else {
      user.hbti = [user.hbti1, user.hbti2, user.hbti3, user.hbti4, user.hbti5];
    }
    delete user.hbti1;
    delete user.hbti2;
    delete user.hbti3;
    delete user.hbti4;
    delete user.hbti5;
  });

  return result;
}

// 내 트레이너 리스트
app.post("/session/getTrainer", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [내 트레이너 리스트 ]`);

  // dereq=decryptAES128(req);
  dereq = req;

  console.log(dereq.body);

  var traineeId = dereq.body.traineeUid;

  var sql =
    `select ts.sid, t.uid, ts.sessionnow,t.trainer_id,name,gender,career,certificate,lesson,LPAD(BIN(usercategory),6,"0") as usercategory ,location, hbti1,hbti2,hbti3,hbti4,hbti5 ` +
    `from TrainSession as ts inner join trainer as t on ts.trainer_id = t.uid  inner join user as u on t.uid=u.uid ` +
    `where trainee_id=?;`;
  var params = [traineeId];

  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";
    var trainerlist = [];

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "내 트레이너 리스트 오류 발생";
    } else {
      if (result.length >= 0) {
        resultCode = 200;

        message = "내 트레이너 리스트 성공";
        trainerlist = changeHbtiList(result);
      }
    }

    res.json({
      code: resultCode,
      message: message,
      trainerlist: trainerlist,
    });
  });
});

// 내 트레이니 리스트
app.post("/session/getTrainee", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [내 트레이니 리스트 ]`);

  // dereq=decryptAES128(req);
  dereq = req;

  console.log(dereq.body);

  var trainerId = dereq.body.trainerUid;

  var sql =
    `select ts.sid, t.uid,ts.sessionnow, t.trainee_id,name,gender,description,LPAD(BIN(usercategory),6,"0") as usercategory ,location, hbti1,hbti2,hbti3,hbti4,hbti5 ` +
    `from TrainSession as ts inner join trainee as t on ts.trainee_id = t.uid  inner join user as u on t.uid=u.uid where trainer_id=?;`;
  var params = [trainerId];

  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";
    var traineelist = [];

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "내 트레이니 리스트 오류 발생";
    } else {
      if (result.length >= 0) {
        resultCode = 200;

        message = "내 트레이니 리스트 성공";

        traineelist = changeHbtiList(result);
      }
    }

    res.json({
      code: resultCode,
      message: message,
      traineelist: traineelist,
    });
  });
});
