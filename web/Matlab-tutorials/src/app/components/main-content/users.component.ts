import { Component, OnInit } from '@angular/core';
import {LoginService} from "../../services/login.service";
import {GroupService} from "../../services/group.service";
import {DialogService} from "ng2-bootstrap-modal";
import {TextInputModal, TextInputModalComponent} from "../text-input-modal/text-input-modal";
import {ConfirmModalComponent} from "../confirm-modal/confirm-modal.component";

@Component({
  selector: 'users',
  templateUrl: './users.component.html',
})
export class UsersComponent implements OnInit {

  canApprove: boolean;

  groups: any[];

  constructor(private loginService : LoginService,
              private groupService: GroupService,
              private dialogService: DialogService) { }

  ngOnInit() {
    this.loginService.details().then(user => {
      this.canApprove = this.loginService.isAdmin(user) || this.loginService.isTeacher(user);
    });

    this.updateGroups();
  }

  updateGroups(){
    return this.groupService.listWithAccess().then((groups: any[]) => {
      this.groups = groups;
    });
  }

  renameGroup(group){
    this.dialogService.addDialog(TextInputModalComponent, {
      title: "Переименовать групппу",
      message: "Имя группы",
      inputValue: group.name
    }).subscribe(result => {
      if(result){
        this.groupService.edit(group.id, {
          name: result
        }).then(result => {
          this.updateGroups();
        })
      }
    });
  }

  createNewGroup($event) {
    $event.preventDefault();

    this.dialogService.addDialog(TextInputModalComponent, {
      title: "Создать новую группу",
      message: "Имя группы",
      inputValue: "Новая группа"
    }).subscribe(result => {
      if(result){
        this.groupService.create({
          name: result
        }).then(result => {
          this.updateGroups();
        })
      }
    });
  }

  deleteGroup(group: any) {
    let disposable = this.dialogService.addDialog(ConfirmModalComponent, {
      title: 'Удалить Группу?',
      message: `Вы уверены что хотите удалить группу "${group.name}"?`
    })
      .subscribe((isConfirmed) => {
        if (isConfirmed) {
          this.groupService.delete(group.id).then(result => {
            return this.updateGroups();
          }).catch(result => {
            return this.updateGroups();
          });
        }
      });

  }
}
