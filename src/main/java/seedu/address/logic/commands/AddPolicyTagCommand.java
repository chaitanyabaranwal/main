package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_POLICIES;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.policy.Policy;
import seedu.address.model.tag.Tag;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Adds a tag to an existing policy in the address book.
 */
public class AddPolicyTagCommand extends Command {

    public static final String COMMAND_WORD = "addpolicytag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds tag to the policy identified "
            + "by the index number used in the last policy listing. "
            + "Parameters: INDEX (must be a positive integer) "
            + "t/ TAG [MORE_TAGS]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + "t/life t/accident";

    private static final String MESSAGE_ADD_TAG_SUCCESS = "Added tag to Policy: %1$s";

    private final Index index;
    private final String[] tags;

    public AddPolicyTagCommand(Index index, String... tags) {
        requireAllNonNull(index, tags);
        this.index = index;
        this.tags = tags;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Policy> lastShownList = model.getFilteredPolicyList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_POLICY_DISPLAYED_INDEX);
        }

        Policy policyToEdit = lastShownList.get(index.getZeroBased());
        Set<Tag> newTags = new HashSet<>(policyToEdit.getTags());

        for (String tag : tags) {
            newTags.add(new Tag(tag));
        }

        Policy editedPolicy = new Policy(
                policyToEdit.getName(),
                policyToEdit.getDescription(),
                policyToEdit.getCoverage(),
                policyToEdit.getPrice(),
                policyToEdit.getStartAge(),
                policyToEdit.getEndAge(),
                policyToEdit.getCriteria(),
                newTags
        );

        model.setPolicy(policyToEdit, editedPolicy);
        model.updateFilteredPolicyList(PREDICATE_SHOW_ALL_POLICIES);

        return new CommandResult(generateSuccessMessage(editedPolicy));
    }

    private String generateSuccessMessage(Policy policyToEdit) {
        return String.format(MESSAGE_ADD_TAG_SUCCESS, policyToEdit);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddPolicyTagCommand)) {
            return false;
        }

        AddPolicyTagCommand e = (AddPolicyTagCommand) other;
        return index.equals(e.index) && Arrays.equals(tags, e.tags);
    }
}
