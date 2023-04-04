import {
  Component,
  OnInit,
  OnDestroy,
  AfterViewInit,
  ViewChild,
} from '@angular/core';
import { WebcamComponent, WebcamImage } from 'ngx-webcam';
import { Subject, Subscription } from 'rxjs';
import { CameraService } from '../camera.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-camera',
  templateUrl: './camera.component.html',
  styleUrls: ['./camera.component.css'],
})
export class CameraComponent implements OnInit, OnDestroy, AfterViewInit {
  //  the view child is used to get the web component
  @ViewChild(WebcamComponent)
  webcam!: WebcamComponent;

  width = 400;
  height = 400;
  pics: string[] = [];
  sub$!: Subscription;
  trigger = new Subject<void>();
  constructor(private router: Router, private cameraSvc: CameraService) {}

  ngOnInit(): void {
    console.log('init ... ' + this.webcam);
  }

  ngOnDestroy(): void {
    this.sub$.unsubscribe();
  }

  // when the web cam gets snapped and the image is loaded into the element (view child), we need to capture the image and send to the memory
  //  hence it has to be completely loaded, before you can capture using the web cam, hence using AfterViewInit
  ngAfterViewInit(): void {
    // if the trigger is being triggered (.next?), it will trigger the web cam service, which has a trigger attribute inside
    // once it trigger, it would mean that you should start getting ready to capture photo
    this.webcam.trigger = this.trigger;
    // hence we subscribe to an object inside the webcam, called imagecapture, so that we can do a snap
    this.sub$ = this.webcam.imageCapture.subscribe(this.snapshot.bind(this));
  }

  snap() {
    this.trigger.next();
  }

  snapshot(webcamImg: WebcamImage) {
    this.cameraSvc.imageData = webcamImg.imageAsDataUrl;
    this.pics.push(webcamImg.imageAsDataUrl);
  }
}
