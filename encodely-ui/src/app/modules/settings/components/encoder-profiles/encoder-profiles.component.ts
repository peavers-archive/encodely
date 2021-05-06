import {Component, OnInit} from '@angular/core';
import {ConfigService} from '../../../../core/services/config.service';
import {EncoderProfile} from '../../../../core/domain/encoder-profile.module';
import {EncoderProfileService} from '../../../../core/services/encoder-profile.service';
import {Select2OptionData} from 'ng-select2';

@Component({
    selector: 'app-encoder-profiles',
    templateUrl: './encoder-profiles.component.html',
    styleUrls: ['./encoder-profiles.component.scss']
})
export class EncoderProfilesComponent implements OnInit {
    select2OptionData: Array<Select2OptionData> = [];

    encoderProfiles: EncoderProfile[] = [];

    encoderProfile: EncoderProfile | undefined;

    constructor(private configService: ConfigService, private encoderProfileService: EncoderProfileService) {
    }

    ngOnInit() {
        this.encoderProfileService.findAll().subscribe((encoderProfiles: EncoderProfile[]) => {
            this.select2OptionData = this.mapToSelect2(encoderProfiles);
            this.encoderProfiles = encoderProfiles;

            const defaultProfile = encoderProfiles.find((p) => p.active);
            if (defaultProfile !== undefined) {
                this.encoderProfile = defaultProfile;
            }
        });
    }

    setEncoderProfileDefault(event: string | string[]) {
        // @ts-ignore
        const value: number = parseInt(event);

        this.encoderProfileService.setDefaultEncoderProfile(this.encoderProfiles[value - 1]);
    }

    createNewEncoderProfile() {
        this.encoderProfileService.createNewEncoderProfile();
    }

    editEncoderProfile(encoderProfile: EncoderProfile) {
        this.encoderProfileService.editEncoderProfile(encoderProfile);
    }

    mapToSelect2(encoderProfiles: EncoderProfile[]): Select2OptionData[] {
        const data: Select2OptionData[] = [];

        for (const encoderProfile of encoderProfiles) {
            data.push({
                id: encoderProfile.id!.toString(),
                text: encoderProfile.name!
            });
        }

        return data;
    }

    getDefault(): string {
        return this.encoderProfile !== undefined ? this.encoderProfile.name! : 'Set default profile';
    }
}
