import { Component, OnInit } from '@angular/core';
import { FormGroup, FormGroupDirective, FormGroupName } from '@angular/forms';

@Component({
  selector: 'jhi-adresse-edit2',
  templateUrl: './adresse-edit2.component.html',
})
export class AdresseEdit2Component implements OnInit {
  editForm!: FormGroup;

  constructor(private rootFormGroup: FormGroupDirective, private formGroupName: FormGroupName) {}

  ngOnInit(): void {
    this.editForm = this.rootFormGroup.getFormGroup(this.formGroupName);
  }
}
