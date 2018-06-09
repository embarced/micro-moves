const assert = require('assert');
const domain = require("../../domain.js");

const Colour = domain.Colour;

describe('Colour unit tests', () => {
    describe('other', () => {
        it('from black to white', () => {
            const result = Colour.other(Colour.BLACK);
            assert.equal(result, Colour.WHITE);
        });
        it('from white to black', () => {
            const result = Colour.other(Colour.BLACK);
            assert.equal(result, Colour.WHITE);
        });
    });
});
