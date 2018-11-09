/**
 * Created by Tianshan on 18-10-03.
 */
function CropperImage() {
    $('#fileType').val(1);
    var img_view = $('#img-view-area');
    img_view.cropper({
        //aspectRatio: 16 / 9,
        autoCrop:true,
        crop: function(event) {
            console.log(event.detail.x);
            console.log(event.detail.y);
            console.log(event.detail.width);
            console.log(event.detail.height);
            console.log(event.detail.rotate);
            console.log(event.detail.scaleX);
            console.log(event.detail.scaleY);
        }
    });

// Get the Cropper.js instance after initialized
    var cropper = img_view.data('cropper');
};

function CropingImage() {
    var img_view = $('#img-view-area');
    return img_view.cropper('getCroppedCanvas');
};
