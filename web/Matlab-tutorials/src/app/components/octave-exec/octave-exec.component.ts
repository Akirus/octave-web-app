import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {OctaveService} from "../../services/octave.service";
import {Router} from "@angular/router";

@Component({
  selector: 'octave-exec',
  templateUrl: './octave-exec.component.html',
  styleUrls: ['./octave-exec.component.css'],
})
export class OctaveExecComponent implements OnInit {
  @Input() code: string;

  loading : boolean = false;
  output: string;
  errors: string;

  constructor(private cdRef: ChangeDetectorRef,
              private octaviaService : OctaveService,
              private router: Router) { }

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

  gotoPlayground(){
    localStorage.setItem('currentCode', this.code);

    this.router.navigate(['/playground']);
  }

  ngOnInit() {
  }

}
