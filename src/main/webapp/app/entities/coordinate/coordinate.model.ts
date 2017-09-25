import { BaseEntity } from './../../shared';

export class Coordinate implements BaseEntity {
    constructor(
        public id?: number,
        public lati?: number,
        public longi?: number,
        public location?: BaseEntity,
    ) {
    }
}
