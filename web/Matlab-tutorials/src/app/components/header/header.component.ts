import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html'
})
export class HeaderComponent implements OnInit {
  title = 'Matlab Tutorials';

  constructor() { }

  ngOnInit() {
  }

}
