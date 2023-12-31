// main.js
var mysql = require("mysql");
var express = require("express");
var bodyParser = require("body-parser");
const path = require("path");
var config = require("./db_info").maindb;
var app = express();
const fs = require("fs");

const fileUpload = require("express-fileupload");
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
app.use(
  fileUpload({
    createParentPath: true,
    limits: {
      //file사이즈 제한
      fileSize: 2 * 1024 * 1024 * 1024,
    },
  })
);

//db 연결
var connection = mysql.createConnection({
  host: config.host,
  user: config.user,
  database: config.database,
  password: config.password,
  port: config.port,
  timezone: config.timezone,
  useUTC: false,
  dateStrings: "date",
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
  // const year = date.getFullYear();
  // const month = String(date.getMonth() + 1).padStart(2, "0");
  // const day = String(date.getDate()).padStart(2, "0");
  // const formattedDate = `${year}-${month}-${day}`;
  // return formattedDate;
  return date;
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
  var sql = "select hbti1,hbti2,hbti3,hbti4 from user where uid= ? ;";
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
            ? [user.hbti1, user.hbti2, user.hbti3, user.hbti4]
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
  var sql = "update user set hbti1=?, hbti2=?, hbti3=?, hbti4=? where uid =?;";
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
              ? [user.hbti1, user.hbti2, user.hbti3, user.hbti4]
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
    'select t.uid,trainer_id,name,gender,career,certificate,lesson,LPAD(BIN(usercategory),6,"0") as usercategory ,location, hbti1,hbti2,hbti3,hbti4 from trainer as t inner join user as u on t.uid=u.uid where (usercategory & b?)=b? and location LIKE ?' +
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
    'select t.uid,trainer_id,name,gender,career,certificate,lesson,LPAD(BIN(usercategory),6,"0") as usercategory ,location, hbti1,hbti2,hbti3,hbti4 ,' +
    " 100-(ABS(hbti1 - ?) + ABS(hbti2 - ?) + ABS(hbti3 - ?) + ABS(hbti4 - ?) ) div 4 as matchingscore " +
    "from trainer as t inner join user as u on t.uid=u.uid where (usercategory & b?)=b? and location LIKE ?" +
    (gender != null ? "and gender=?" : "") +
    "and u.hbti1 is not null order by matchingscore desc;";
  var params = [
    hbti[0],
    hbti[1],
    hbti[2],
    hbti[3],
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
      user.hbti = [user.hbti1, user.hbti2, user.hbti3, user.hbti4];
    }
    delete user.hbti1;
    delete user.hbti2;
    delete user.hbti3;
    delete user.hbti4;
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
    `select ts.sid, t.uid, ts.sessionnow,t.trainer_id,name,gender,career,certificate,lesson,LPAD(BIN(usercategory),6,"0") as usercategory ,location, hbti1,hbti2,hbti3,hbti4 ` +
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
    `select ts.sid, t.uid,ts.sessionnow, t.trainee_id,name,gender,description,LPAD(BIN(usercategory),6,"0") as usercategory ,location, hbti1,hbti2,hbti3,hbti4 ` +
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

// 글작성
app.post("/sns/createpost", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [글 작성 ]`);

  // dereq=decryptAES128(req);
  dereq = req;

  console.log(dereq.body);

  var uid = dereq.body.uid;
  var title = dereq.body.title;
  var content = dereq.body.content;
  var category = dereq.body.category;
  var image;
  if (!dereq.files) {
    image = null;
  } else {
    image = dereq.files.image;
  }
  var sql =
    image == null
      ? "INSERT INTO post (uid,title,content,category,create_at) VALUES (?,?,?,b?,now());"
      : "INSERT INTO post (uid,title,content,category,create_at,image) VALUES (?,?,?,b?,now(),1);";
  var params = [uid, title, content, category];

  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";
    var postid = null;

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "글 작성-오류";
    } else {
      if (result.insertId >= 0) {
        resultCode = 200;
        message = "글 작성 완료";
        postid = result.insertId;
        saveImageSns(image, postid);
      }
    }

    res.json({
      code: resultCode,
      message: message,
      postid: postid,
    });
  });
});

// 글 삭제 - 이미지 삭제 안만들어져있음
app.post("/sns/deletepost", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [글 삭제...]`);
  console.log(req.body);
  // dereq=decryptAES128(req);
  dereq = req;

  var uid = dereq.body.uid;
  var pid = dereq.body.pid;

  // 글 삭제 코드
  var sql = "delete FROM post where uid = ? AND pid= ?;";
  var params = [uid, pid];

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
      message = "글 삭제 완료";
      isDeleted = true;
      deleteImageSns(pid);
    } else {
      // 영향을 받은 행이 없을 경우 삭제 실패로 간주
      resultCode = 404;
      message = "글 삭제 오류";
    }

    res.json({
      code: resultCode,
      message: message,
      isDeleted: isDeleted,
    });
  });
});

// 글 수정
app.post("/sns/editpost", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [글 수정 ]`);

  // dereq=decryptAES128(req);
  dereq = req;

  console.log(dereq.body);

  var pid = dereq.body.pid;
  var uid = dereq.body.uid;
  var title = dereq.body.title;
  var content = dereq.body.content;
  var isImagechange = dereq.body.isImagechange;
  var image; // 이미지 파일
  if (!dereq.files) {
    image = null;
  } else {
    image = dereq.files.image;
    isImagechange = true;
  }
  var category = dereq.body.category;

  var sql = isImagechange
    ? image == null
      ? "update post set title=?,content=?,category=b?, create_at=now(), image=0 where uid = ? and pid=?"
      : "update post set title=?,content=?,category=b?, create_at=now(),image=1 where uid = ? and pid=?"
    : "update post set title=?,content=?,category=b?, create_at=now() where uid = ? and pid=?";
  var params = [title, content, category, uid, pid];

  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "글 수정-오류";
    } else {
      if (result.affectedRows >= 0) {
        resultCode = 200;
        message = "글 수정 완료";
        if (isImagechange && image != null) {
          saveImageSns(image, pid);
        }
      }
    }

    res.json({
      code: resultCode,
      message: message,
    });
  });
});

// 글 리스트
app.post("/sns/getPost", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [get post list]`);

  // dereq=decryptAES128(req);
  dereq = req;

  console.log(dereq.body);

  var uid = dereq.body.uid;
  var category = dereq.body.category;

  var sql =
    `select pid,u.uid,name,if(role = "1",'true','false') as isTrainee, LPAD(BIN(po.category),6,"0") as postcategory,title,content,create_at,image from post as po inner join user as u on po.uid=u.uid where true` +
    (category == null ? "" : ` and (category & b?)=b?`) +
    (uid == null ? "" : ` and u.uid=?`) +
    " order by create_at desc ;";
  var params = [];
  if (category != null) {
    params.push(category);
    params.push(category);
  }
  if (uid != null) {
    params.push(uid);
  }

  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";
    var postlist = [];

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "글 리스트 오류 발생";
    } else {
      if (result.length >= 0) {
        resultCode = 200;
        message = "글 리스트 성공";
        postlist = result;
      }
    }

    res.json({
      code: resultCode,
      message: message,
      postlist: postlist,
    });
  });
});

// 트레이너 프로필
app.post("/trainer/getProfile", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [트레이너 프로필 get...]`);
  console.log(req.body);
  // dereq=decryptAES128(req);
  dereq = req;

  var trainerUid = dereq.body.trainerUid;

  // 트레이너 프로필 쿼리
  var sql =
    'select t.uid,trainer_id,name,gender,career,certificate,lesson,LPAD(BIN(usercategory),6,"0") as usercategory ,location, hbti1,hbti2,hbti3,hbti4 from trainer as t inner join user as u on t.uid=u.uid where u.uid=? ;';

  params = [trainerUid];
  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";
    var trainerProfile = null;

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "오류 발생";
    } else {
      if (result.length > 0) {
        resultCode = 200;
        message = "해당하는 트레이너 리턴";
        trainerProfile = result[0];

        if (trainerProfile["hbti1"] == null) {
          trainerProfile.hbti = null;
        } else {
          trainerProfile.hbti = [
            trainerProfile.hbti1,
            trainerProfile.hbti2,
            trainerProfile.hbti3,
            trainerProfile.hbti4,
          ];
        }
        delete trainerProfile.hbti1;
        delete trainerProfile.hbti2;
        delete trainerProfile.hbti3;
        delete trainerProfile.hbti4;
      }
    }

    res.json({
      code: resultCode,
      message: message,
      trainerProfile: trainerProfile,
    });
  });
});

// 댓글글작성
app.post("/sns/createComment", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [댓글 작성 ]`);

  // dereq=decryptAES128(req);
  dereq = req;

  console.log(dereq.body);

  var uid = dereq.body.uid;
  var pid = dereq.body.pid;
  var content = dereq.body.content;

  var sql =
    "INSERT INTO comment (uid,pid,content,create_at) VALUES (?,?,?,now());";
  var params = [uid, pid, content];

  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";
    var commentid = null;

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "댓글 작성-오류";
    } else {
      if (result.insertId >= 0) {
        resultCode = 200;
        message = "글 작성 완료";
        commentid = result.insertId;
      }
    }

    res.json({
      code: resultCode,
      message: message,
      commentid: commentid,
    });
  });
});

// 댓글 삭제
app.post("/sns/deleteComment", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [댓글 삭제...]`);
  console.log(req.body);
  // dereq=decryptAES128(req);
  dereq = req;

  var uid = dereq.body.uid;
  var cid = dereq.body.cid;

  // 글 삭제 코드
  var sql = "delete FROM comment where uid = ? AND cid= ?;";
  var params = [uid, cid];

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
      message = "댓글 삭제 완료";
      isDeleted = true;
    } else {
      // 영향을 받은 행이 없을 경우 삭제 실패로 간주
      resultCode = 404;
      message = "댓글 삭제 오류";
    }

    res.json({
      code: resultCode,
      message: message,
      isDeleted: isDeleted,
    });
  });
});

// 댓글 수정
app.post("/sns/editComment", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [댓글 수정 ]`);

  // dereq=decryptAES128(req);
  dereq = req;

  console.log(dereq.body);

  var cid = dereq.body.cid;
  var uid = dereq.body.uid;
  var content = dereq.body.content;

  var sql =
    "update comment set content=?, create_at=now() where cid = ? and uid=?";
  var params = [content, cid, uid];

  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "댓글 수정-오류";
    } else {
      if (result.affectedRows >= 0) {
        resultCode = 200;
        message = "댓글 수정 완료";
      }
    }

    res.json({
      code: resultCode,
      message: message,
    });
  });
});

// 글의 댓글 리스트
app.post("/sns/getComment", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [get comment list]`);

  // dereq=decryptAES128(req);
  dereq = req;

  console.log(dereq.body);

  var pid = dereq.body.pid;

  var sql = `select pid,u.uid,name,if(role = "1",'true','false') as isTrainee, content,create_at from comment as co inner join user as u on co.uid=u.uid where pid=? order by create_at ;`;
  var params = [pid];

  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";
    var comments = [];

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "댓글 리스트 오류 발생";
    } else {
      if (result.length >= 0) {
        resultCode = 200;
        message = "댓글 리스트 성공";
        comments = result;
      }
    }

    res.json({
      code: resultCode,
      message: message,
      comments: comments,
    });
  });
});

// 리뷰 작성
app.post("/Review/Create", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [리뷰 작성 ]`);

  // dereq=decryptAES128(req);
  dereq = req;

  console.log(dereq.body);

  var traineruid = dereq.body.traineruid;
  var traineeuid = dereq.body.traineeuid;
  var content = dereq.body.content;
  var score = dereq.body.score;

  var sql =
    "INSERT INTO Review (traineruid,traineeuid,content,score,create_at) VALUES (?,?,?,?,now());";
  var params = [traineruid, traineeuid, content, score];

  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "리뷰 작성-오류";
    } else {
      if (result.insertId >= 0) {
        resultCode = 200;
        message = "리뷰 작성 완료";
      }
    }

    res.json({
      code: resultCode,
      message: message,
    });
  });
});

// 리뷰 삭제
app.post("/Review/Delete", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [리뷰 삭제...]`);
  console.log(req.body);
  // dereq=decryptAES128(req);
  dereq = req;

  var traineruid = dereq.body.traineruid;
  var traineeuid = dereq.body.traineeuid;

  // 글 삭제 코드
  var sql = "delete FROM Review where traineruid = ? AND traineeuid= ?;";
  var params = [traineruid, traineeuid];

  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "리뷰 삭제 오류 발생";
    } else if (result.affectedRows > 0) {
      // 영향을 받은 행의 개수가 1 이상일 경우 삭제 성공으로 간주
      resultCode = 200;
      message = "리뷰 삭제 완료";
    } else {
      // 영향을 받은 행이 없을 경우 삭제 실패로 간주
      resultCode = 404;
      message = "리뷰 삭제 오류";
    }

    res.json({
      code: resultCode,
      message: message,
    });
  });
});

// 리뷰 수정
app.post("/Review/Edit", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [리뷰 수정 ]`);

  // dereq=decryptAES128(req);
  dereq = req;

  console.log(dereq.body);

  var traineruid = dereq.body.traineruid;
  var traineeuid = dereq.body.traineeuid;
  var content = dereq.body.content;
  var score = dereq.body.score;

  var sql =
    "update Review set content=?, score=?,create_at=now() where traineruid = ? and traineeuid=?";
  var params = [content, score, traineruid, traineeuid];

  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "리뷰 수정-오류";
    } else {
      if (result.affectedRows >= 0) {
        resultCode = 200;
        message = "리뷰 수정 완료";
      }
    }

    res.json({
      code: resultCode,
      message: message,
    });
  });
});

// 트레이너의 리뷰 리스트
app.post("/Review/Get", function (req, res) {
  console.log(`${new Date().toLocaleString("ko-kr")} [get review list]`);

  // dereq=decryptAES128(req);
  dereq = req;

  console.log(dereq.body);

  var traineruid = dereq.body.traineruid;

  var sql = `select u.uid,name,score ,content,create_at from Review as re inner join user as u on re.traineeuid=u.uid where traineruid=? order by create_at;`;
  var params = [traineruid];

  connection.query(sql, params, function (err, result) {
    var resultCode = 404;
    var message = "에러가 발생했습니다";
    var reviews = [];

    if (err) {
      console.log(err);
      resultCode = 400;
      message = "리뷰 리스트 오류 발생";
    } else {
      if (result.length >= 0) {
        resultCode = 200;
        message = "리뷰 리스트 성공";
        reviews = result;
      }
    }

    res.json({
      code: resultCode,
      message: message,
      reviews: reviews,
    });
  });
});

function saveImageSns(image, index) {
  try {
    let f = image;
    f.mv("./uploads/sns/" + `${index}.png`);
    return true;
  } catch (error) {
    return false;
  }
  return false;
}

function deleteImageSns(index) {
  try {
    fs.unlink("./uploads/sns/" + `${index}.png`, (err) => {
      if (err) {
        console.error(err);
        return false;
      }

      console.log("File is deleted.");
    });
  } catch (error) {
    return false;
  }
  return false;
}

app.get("/snsimage/:pid", (req, res) => {
  const pid = req.params.pid;
  res.sendFile(path.join(__dirname, "uploads/sns", `${pid}.png`));
});
