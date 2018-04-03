import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {OctaveService} from "../../../services/octave.service";

@Component({
  selector: 'app-playground',
  templateUrl: './playground.component.html',
  styleUrls: ['./playground.component.css'],
})
export class PlaygroundComponent implements OnInit {

  code : string;
  loading : boolean;
  output: string;
  errors: string;

  editorOptions:any = {enableBasicAutocompletion: true};

  runCode(){
    if(this.code.trim().length > 0) {
      this.loading = true;
      this.cdRef.detectChanges();

      localStorage.setItem('currentCode', this.code);

      this.octaviaService.execute(this.code).then(result => {
        this.output = result.output;
        this.errors = result.errors;
        this.loading = false;
        this.cdRef.detectChanges();
      });
    }
  }

  constructor(private cdRef: ChangeDetectorRef, private octaviaService : OctaveService) { }

  ngOnInit() {
    let code = localStorage.getItem("currentCode");

    if(code && code.trim().length > 0) {
      this.code = code;
    }

  }

}
