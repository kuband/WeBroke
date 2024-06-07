import { Component, OnInit, ElementRef } from '@angular/core';
import { Location, LocationStrategy, PathLocationStrategy } from '@angular/common';

@Component({
  selector: 'app-headers',
  templateUrl: './headers.component.html',
  styleUrls: ['./headers.component.scss']
})
export class HeadersComponent implements OnInit {
    icon_video_button_class: string = 'fa-play';
    text_video_button: string = 'Play Video';
    private toggleButton: any;
    private sidebarVisible: boolean;

    constructor(public location: Location, private element : ElementRef) {
      this.sidebarVisible = false;
  }

  ngOnInit() {
    //   $('[data-toggle="video"]').click(function(){
    //       id_video = $(this).data('video');
    //       video = $('#' + id_video).get(0);
      //
    //       parent = $(this).parent('div').parent('div');
      //
    //       if(video.paused){
    //           video.play();
    //           $(this).html('<i class="fa fa-pause"></i> Pause Video');
    //           parent.addClass('state-play');
    //       } else {
    //           video.pause();
    //           $(this).html('<i class="fa fa-play"></i> Play Video');
    //           parent.removeClass('state-play');
    //       }
    //   });
    const navbar: HTMLElement = this.element.nativeElement;
    this.toggleButton = navbar.getElementsByClassName('navbar-toggler')[1];
  }

  playBackgroundVideo(event){
      var id_video = document.getElementById('video-source') as HTMLVideoElement;
      var parent = event.target.parentElement.parentElement;
    if(id_video.paused){
         id_video.play();
        this.text_video_button = 'Pause Video';
        this.icon_video_button_class = 'fa-pause';
     } else {
         id_video.pause();
        this.text_video_button = 'Play Video';
        this.icon_video_button_class = 'fa-play';
     }
  }

  sidebarOpen() {
    const toggleButton = this.toggleButton;
    const html = document.getElementsByTagName('html')[0];
    setTimeout(function(){
        toggleButton.classList.add('toggled');
    }, 500);
    html.classList.add('nav-open');

    this.sidebarVisible = true;
  };

  sidebarClose() {
      const html = document.getElementsByTagName('html')[0];
      // console.log(html);
      this.toggleButton.classList.remove('toggled');
      this.sidebarVisible = false;
      html.classList.remove('nav-open');
  };

  sidebarToggle() {
      // const toggleButton = this.toggleButton;
      // const body = document.getElementsByTagName('body')[0];
      if (this.sidebarVisible === false) {
          this.sidebarOpen();
      } else {
          this.sidebarClose();
      }
  };

}
