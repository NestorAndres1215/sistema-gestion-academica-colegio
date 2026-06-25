import { Service } from '@angular/core';
import Swal, {
    SweetAlertIcon,
} from 'sweetalert2';

@Service()
export class AlertService {

    private readonly confirmButtonColor = '#3085d6';
    private readonly cancelButtonColor = '#d33';

    private show(icon: SweetAlertIcon, title: string, text?: string) {
        return Swal.fire({
            icon,
            title,
            text,
            confirmButtonColor: this.confirmButtonColor
        });
    }

    success(title: string, text?: string) {
        return this.show('success', title, text);
    }

    error(title: string, text?: string) {
        return this.show('error', title, text);
    }

    warning(title: string, text?: string) {
        return this.show('warning', title, text);
    }

    info(title: string, text?: string) {
        return this.show('info', title, text);
    }

    async confirm(
        title: string,
        text?: string,
        confirmButtonText = 'Aceptar',
        cancelButtonText = 'Cancelar'
    ): Promise<boolean> {

        const result = await Swal.fire({
            title,
            text,
            icon: 'question',
            showCancelButton: true,
            confirmButtonText,
            cancelButtonText,
            confirmButtonColor: this.confirmButtonColor,
            cancelButtonColor: this.cancelButtonColor
        });

        return result.isConfirmed;
    }

    loading(title = 'Cargando...') {
        Swal.fire({
            title,
            allowOutsideClick: false,
            allowEscapeKey: false,
            didOpen: () => Swal.showLoading()
        });
    }

    close() {
        Swal.close();
    }
}