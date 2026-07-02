import { inject, Service } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { AlertService } from './alert.service';

@Service()
export class FormValidationService {

    private readonly alertService = inject(AlertService);

    // VALIDAR SI LOS CAMPOS ESTAN INCOMPLETOS

    validate(form: FormGroup): boolean {
        if (form.invalid) {
            form.markAllAsTouched();
            this.alertService.warning('Campos incompletos', 'Faltan campos');
            return false;
        }

        return true;
    }
}
