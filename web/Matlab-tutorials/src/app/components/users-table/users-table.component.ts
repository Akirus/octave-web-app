import {Component, Input, OnInit} from '@angular/core';
import {LoginService} from "../../services/login.service";
import {GroupService} from "../../services/group.service";

@Component({
  selector: 'users-table',
  templateUrl: './users-table.component.html',
  styleUrls: ['./users-table.component.css'],
  providers: [LoginService]
})
export class UsersTableComponent implements OnInit {

  @Input() filter: string;
  @Input() groupId: string = null;

  usersList: any = [];

  canApprove: boolean;
  isTeacher: boolean;

  private static refresh: boolean = false;

  constructor(private loginService: LoginService,
              private groupService: GroupService) { }

  get users(){
    if(UsersTableComponent.refresh){
      this.updateList();
    }

    return this.usersList;
  }

  ngOnInit() {
    this.updateList();
  }

  private getUsers(){
    if(this.groupId == null){
      return this.loginService.list(this.filter);
    }
    return this.groupService.students(this.groupId);
  }

  updateList(){
    this.loginService.details().then(user => {
      this.canApprove = this.loginService.isAdmin(user) || this.loginService.isTeacher(user);
      this.isTeacher = this.loginService.isTeacher(user);

      return this.getUsers();
    }).then(result => {
      this.processList(result);
    });
  }

  approve(user){
    return this.loginService.approve(user.id).then(result => {
      return this.getUsers();
    }).then( result => {
      this.processList(result);
    });
  }

  reject(user){
    return this.loginService.reject(user.id).then(result => {
      return this.getUsers();
    }).then( result => {
      this.processList(result);
    });
  }

  private processList(result){
    this.usersList = result;
    let anyNeedToBeApproved = false;
    for(let user of this.usersList){
      if(!user.enabled){
        anyNeedToBeApproved = true;
        break;
      }
    }
    console.log("any " + anyNeedToBeApproved);
    this.canApprove = this.canApprove && anyNeedToBeApproved;
  }

  displayRoles(roleArray){
    let names = roleArray.map(role => role.name);
    return names.join(',');
  }

  names = {
    "Teacher" : "Преподаватель",
    "Admin" : "Администратор",
    "User" : "Студент"
  };

  translateRoles(roles){
    let names = roles.split(',');
    return names.map(name => this.names[name]).join(', ');
  }

}
