<?php

 require_once 'DbConnect.php';

 $response = array();

 if (isset($_GET['apicall'])) {
     switch ($_GET['apicall']) {

 case 'signup':

 if (isTheseParametersAvailable(array('nama_depan', 'nama_belakang', 'username', 'email_user', 'password'))) {

 //getting the values

     $nama_depan = $_POST['nama_depan'];
     $nama_belakang = $_POST['nama_belakang'];
     $username = $_POST['username'];
     $email_user = $_POST['email_user'];
     $password = md5($_POST['password']);


     //checking if the user is already exist with this username or email
     //as the email and username should be unique for every user
     $stmt = $conn->prepare("SELECT id_user FROM user WHERE username = ? OR email_user = ?");
     $stmt->bind_param("ss", $username, $email_user);
     $stmt->execute();
     $stmt->store_result();

     //if the user already exist in the database
     if ($stmt->num_rows > 0) {
         $response['error'] = true;
         $response['message'] = 'User already registered';
         $stmt->close();
     } else {

       //if user is new creating an insert query
         $stmt = $conn->prepare("INSERT INTO user(username, email_user, password, status) VALUES (?, ?, ?, 'M');");
         $stmt1 = $conn->prepare("INSERT INTO mahasiswa(id_user, nama_depan, nama_belakang) VALUES(LAST_INSERT_ID(), ?, ?);");
         $stmt->bind_param("sss", $username, $email_user, $password);
         $stmt1->bind_param("ss", $nama_depan, $nama_belakang);
         $result= $stmt->execute();
         $result1= $stmt1->execute();
         //if the user is successfully added to the database
         if ($result && $result1) {

            //fetching the user back
             $stmt = $conn->prepare("SELECT a.id_user, a.username, a.email_user, b.id_mhs, b.nama_depan, b.nama_belakang FROM user a, mahasiswa b WHERE a.id_user=b.id_user and a.username = ?");
             $stmt->bind_param("s", $username);
             $stmt->execute();
             $stmt->bind_result($id_user, $username, $email_user, $id_mhs, $nama_depan, $nama_belakang);
             $stmt->fetch();

             $user = array(
               'id_user'=>$id_user,
               'username'=>$username,
               'email_user'=>$email_user
             );

             $mahasiswa = array(
               'id_mhs' => $id_mhs,
               'id_user' => $id_user,
               'nama_depan' => $nama_depan,
               'nama_belakang' => $nama_belakang

             );

             $stmt->close();

             //adding the user data in response
             $response['error'] = false;
             $response['message'] = 'User registered successfully';
             $response['user'] = $user;
             $response['mahasiswa'] = $mahasiswa;
         }
     }
 } else {
     $response['error'] = true;
     $response['message'] = 'required parameters are not available';
 }

 break;

 case 'login':

 if (isTheseParametersAvailable(array('username', 'password'))) {
     $username = $_POST['username'];
     $password = md5($_POST['password']);

     $stmt = $conn->prepare("SELECT a.id_user, a.username, a.email_user,b.id_mhs, b.nama_depan,b.nama_belakang FROM user a, mahasiswa b WHERE a.id_user=b.id_user and a.username = ? AND a.password = ?");
     $stmt->bind_param("ss", $username, $password);

     $stmt->execute();

     $stmt->store_result();

     if ($stmt->num_rows > 0) {
         $stmt->bind_result($id_user, $username, $email, $id_mhs, $namaDepan, $namaBelakang);
         $stmt->fetch();

         $user = array(
           'id_user'=>$id_user,
           'username'=>$username,
           'email'=>$email
         );
         $mahasiswa = array(
           'id_user' => $id_user,
           'id_mhs' => $id_mhs,
           'namaDepan' => $namaDepan,
           'namaBelakang' => $namaBelakang

         );

         $response['error'] = false;
         $response['message'] = 'Login successfull';
         $response['user'] = $user;
         $response['mahasiswa'] = $mahasiswa;
     } else {
         $response['error'] = false;
         $response['message'] = 'Invalid username or password';
     }
 }
 break;

 case 'lowongan_list':

 $stmt = $conn->prepare("SELECT a.id_lowongan, a.nama_lowongan, a.id_perusahaan, a.waktu_input, a.lokasi, b.nama_perusahaan, b.logo FROM lowongan a, perusahaan b WHERE a.id_perusahaan=b.id_perusahaan ORDER BY a.waktu_input DESC;");

//executing the query
$stmt->execute();

//binding results to the query
$stmt->bind_result($id_lowongan, $nama_lowongan, $id_perusahaan, $waktu_input,$lokasi, $nama_perusahaan, $logo);

$response = array();

//traversing through all the result
while ($stmt->fetch()) {
    $temp = array();
    $temp['id_lowongan'] = $id_lowongan;
    $temp['nama_lowongan'] = $nama_lowongan;
    $temp['id_perusahaan'] = $id_perusahaan;
    $temp['waktu_input'] = $waktu_input;
    $temp['lokasi'] = $lokasi;
    $temp['nama_perusahaan'] = $nama_perusahaan;
    $temp['logo'] = $logo;
    array_push($response, $temp);
}


 break;

 default:
 $response['error'] = true;
 $response['message'] = 'Invalid Operation Called';
 }
 } else {
     $response['error'] = true;
     $response['message'] = 'Invalid API Call';
 }

 echo json_encode($response, JSON_UNESCAPED_SLASHES);

 function isTheseParametersAvailable($params)
 {
     foreach ($params as $param) {
         if (!isset($_POST[$param])) {
             return false;
         }
     }
     return true;
 }
