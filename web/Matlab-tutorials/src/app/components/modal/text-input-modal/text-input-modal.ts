import { Component, OnInit } from '@angular/core';
import {DialogComponent, DialogService} from "ng2-bootstrap-modal";
import {Observable} from "rxjs/Observable";

export interface TextInputModal {
  title:string;
  message:string;
  inputValue: string;
}

@Component({
  selector: 'app-text-input-modal',
  templateUrl: './text-input-modal.html',
  styleUrls: ['./text-input-modal.css']
})

export class TextInputModalComponent extends DialogComponent<TextInputModal, string> implements TextInputModal {
  title: string;
  message: string;
  inputValue: string;
  constructor(dialogService: DialogService) {
    super(dialogService);
  }
  confirm() {
    this.result = this.inputValue;
    this.close();
  }

}
