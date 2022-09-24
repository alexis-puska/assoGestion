import { Component } from '@angular/core';
import {
  AbstractControl,
  ControlValueAccessor,
  FormBuilder,
  NG_VALIDATORS,
  NG_VALUE_ACCESSOR,
  ValidationErrors,
  Validator,
  Validators,
} from '@angular/forms';
import { Subscription } from 'rxjs';

@Component({
  selector: 'jhi-adresse-edit',
  templateUrl: './adresse-edit.component.html',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      multi: true,
      useExisting: AdresseEditComponent,
    },
    {
      provide: NG_VALIDATORS,
      multi: true,
      useExisting: AdresseEditComponent,
    },
  ],
})
export class AdresseEditComponent implements ControlValueAccessor, Validator {
  editForm = this.fb.group({
    id: [],
    numero: [null, [Validators.required]],
    rue: [null, [Validators.required]],
    codePostale: [null, [Validators.required]],
    ville: [null, [Validators.required]],
  });

  onChangeSubs: Subscription[] = [];

  constructor(private fb: FormBuilder) {}

  registerOnChange(onChange: any): void {
    this.editForm.valueChanges.subscribe(onChange);
  }

  registerOnTouched(onTouched: any): void {
    this.onTouched = onTouched;
  }

  setDisabledState(disabled: boolean): void {
    if (disabled) {
      this.editForm.disable();
    } else {
      this.editForm.enable();
    }
  }

  writeValue(value: any): void {
    if (value) {
      this.editForm.setValue(value, { emitEvent: false });
    }
  }

  validate(control: AbstractControl): ValidationErrors | null {
    console.log(control);
    if (this.editForm.valid) {
      return null;
    }
    let errors: any = {};
    errors = this.addControlErrors(errors, 'id');
    errors = this.addControlErrors(errors, 'numero');
    errors = this.addControlErrors(errors, 'rue');
    errors = this.addControlErrors(errors, 'codePostale');
    errors = this.addControlErrors(errors, 'ville');
    return errors;
  }

  addControlErrors(allErrors: any, controlName: string): ValidationErrors | null {
    const errors = { ...allErrors };

    const controlErrors = this.editForm.controls[controlName].errors;

    if (controlErrors) {
      errors[controlName] = controlErrors;
    }

    return errors;
  }

  private onTouched(): void {}
}
