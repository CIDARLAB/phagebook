function saveProfilePicture() {
        var clothoId = getCookie("clothoId");
        var formData = new FormData();
        var selectedFile = document.getElementById('uploadPicture').files[0];
        formData.append('profilePicture', selectedFile);
        formData.append('profilePictureName', selectedFile.name);
        formData.append('clothoId', clothoId);
        console.log(selectedFile);
        console.log(selectedFile.name);

        $.ajax({
            type: 'POST',
            url: "../uploadProfilePicture",
            data: formData,
            cache: false,
            contentType: false,
            processData: false,
            success: function (response) {
                console.log("inside ajax post uploadProfPic success");
                console.log(response);
            },
            error: function (response) {
                console.log("inside ajax post uploadProfPic error");
            }
        });
};


