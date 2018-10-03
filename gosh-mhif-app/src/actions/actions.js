
export const caseAction = (state = {}, action) => {
    switch (action.type) {
        case 'SWITCH-CASE':
            let newVals = action.values;
            return newVals;
        default:
            return state;
    }
};

export default caseAction;
