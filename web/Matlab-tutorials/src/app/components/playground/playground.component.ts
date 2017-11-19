import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-playground',
  templateUrl: './playground.component.html',
  styleUrls: ['./playground.component.css']
})
export class PlaygroundComponent implements OnInit {

  code : string;
  loading : boolean;
  output: string;
  errors: string;

  constructor() { }

  ngOnInit() {

  }

}
