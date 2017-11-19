import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {OctaveService} from "../../services/octave.service";

@Component({
  selector: 'octave-exec',
  templateUrl: './octave-exec.component.html',
  styleUrls: ['./octave-exec.component.css'],
  providers : [OctaveService]
})
export class OctaveExecComponent implements OnInit {
  @Input() code: string;

  loading : boolean = false;
  output: string;
  errors: string;

  constructor(private cdRef: ChangeDetectorRef, private octaviaService : OctaveService) { }

  runCode(){
    console.log("Run");
    this.loading = true;
    this.cdRef.detectChanges();

    this.octaviaService.execute(this.code).then(result => {
        this.output = result.output;
        this.errors = result.errors;
        this.loading = false;
        this.cdRef.detectChanges();
    });
  }

  ngOnInit() {
  }

}
