/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package net.java.sip.communicator.plugin.addrbook.macosx;

import java.util.*;
import java.util.regex.*;

import net.java.sip.communicator.plugin.addrbook.*;
import net.java.sip.communicator.service.contactsource.*;
import net.java.sip.communicator.service.protocol.*;
import net.java.sip.communicator.util.*;

/**
 * Implements <tt>ContactQuery</tt> for the Address Book of Mac OS X.
 *
 * @author Lyubomir Marinov
 */
public class MacOSXAddrBookContactQuery
    extends AbstractAddrBookContactQuery<MacOSXAddrBookContactSourceService>
{
    /**
     * The <tt>Logger</tt> used by the <tt>MacOSXAddrBookContactQuery</tt> class
     * and its instances for logging output.
     */
    private static final Logger logger
        = Logger.getLogger(MacOSXAddrBookContactQuery.class);

    /**
     * The properties of <tt>ABPerson</tt> which are to be queried by the
     * <tt>MacOSXAddrBookContactQuery</tt> instances.
     */
    public static final long[] ABPERSON_PROPERTIES
        = new long[]
        {
            kABAIMInstantProperty(),
            kABEmailProperty(),
            kABFirstNameProperty(),
            kABFirstNamePhoneticProperty(),
            kABICQInstantProperty(),
            kABJabberInstantProperty(),
            kABLastNameProperty(),
            kABLastNamePhoneticProperty(),
            kABMiddleNameProperty(),
            kABMiddleNamePhoneticProperty(),
            kABMSNInstantProperty(),
            kABNicknameProperty(),
            kABPhoneProperty(),
            kABYahooInstantProperty(),
            kABPersonFlags(),
            kABOrganizationProperty(),
            kABMaidenNameProperty(),
            kABBirthdayProperty(),
            kABJobTitleProperty(),
            kABHomePageProperty(),
            kABURLsProperty(),
            kABCalendarURIsProperty(),
            kABAddressProperty(),
            kABOtherDatesProperty(),
            kABRelatedNamesProperty(),
            kABDepartmentProperty(),
            kABNoteProperty(),
            kABSocialProfileProperty(),
            kABTitleProperty(),
            kABSuffixProperty()
        };

    /**
     * The index of the <tt>kABAIMInstantProperty</tt> <tt>ABPerson</tt>
     * property in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABAIMInstantProperty = 0;

    /**
     * The index of the <tt>kABEmailProperty</tt> <tt>ABPerson</tt> property in
     * {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABEmailProperty = 1;

    /**
     * The index of the <tt>kABFirstNameProperty</tt> <tt>ABPerson</tt> property
     * in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABFirstNameProperty = 2;

    /**
     * The index of the <tt>kABFirstNamePhoneticProperty</tt> <tt>ABPerson</tt>
     * property in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABFirstNamePhoneticProperty = 3;

    /**
     * The index of the <tt>kABICQInstantProperty</tt> <tt>ABPerson</tt>
     * property in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABICQInstantProperty = 4;

    /**
     * The index of the <tt>kABJabberInstantProperty</tt> <tt>ABPerson</tt>
     * property in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABJabberInstantProperty = 5;

    /**
     * The index of the <tt>kABLastNameProperty</tt> <tt>ABPerson</tt> property
     * in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABLastNameProperty = 6;

    /**
     * The index of the <tt>kABLastNamePhoneticProperty</tt> <tt>ABPerson</tt>
     * property in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABLastNamePhoneticProperty = 7;

    /**
     * The index of the <tt>kABMiddleNameProperty</tt> <tt>ABPerson</tt>
     * property in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABMiddleNameProperty = 8;

    /**
     * The index of the <tt>kABMiddleNamePhoneticProperty</tt> <tt>ABPerson</tt>
     * property in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABMiddleNamePhoneticProperty = 9;

    /**
     * The index of the <tt>kABMSNInstantProperty</tt> <tt>ABPerson</tt>
     * property in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABMSNInstantProperty = 10;

    /**
     * The index of the <tt>kABNicknameProperty</tt> <tt>ABPerson</tt> property
     * in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABNicknameProperty = 11;

    /**
     * The index of the <tt>kABOrganizationProperty</tt> <tt>ABPerson</tt>
     * property in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABOrganizationProperty = 15;

    /**
     * The index of the <tt>kABPersonFlags</tt> <tt>ABPerson</tt> property in
     * {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABPersonFlags = 14;

    /**
     * The index of the <tt>kABPhoneProperty</tt> <tt>ABPerson</tt> property in
     * {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABPhoneProperty = 12;

    /**
     * The flag which indicates that an <tt>ABRecord</tt> is to be displayed as
     * a company.
     */
    private static final long kABShowAsCompany = 1;

    /**
     * The mask which extracts the <tt>kABShowAsXXX</tt> flag from the
     * <tt>personFlags</tt> of an <tt>ABPerson</tt>.
     */
    private static final long kABShowAsMask = 7;

    /**
     * The index of the <tt>kABYahooInstantProperty</tt> <tt>ABPerson</tt>
     * property in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABYahooInstantProperty = 13;

    /**
     * The index of the <tt>kABMaidenNameProperty</tt> <tt>ABPerson</tt>
     * property in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABMaidenNameProperty = 16;

    /**
     * The index of the <tt>kABBirthdayProperty</tt> <tt>ABPerson</tt>
     * property in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABBirthdayProperty = 17;

    /**
     * The index of the <tt>kABJobTitleProperty</tt> <tt>ABPerson</tt>
     * property in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABJobTitleProperty = 18;

    /**
     * The index of the <tt>kABHomePageProperty</tt> <tt>ABPerson</tt>
     * property in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABHomePageProperty = 19;

    /**
     * The index of the <tt>kABURLsProperty</tt> <tt>ABPerson</tt>
     * property in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABURLsProperty = 20;

    /**
     * The index of the <tt>kABCalendarURIsProperty</tt> <tt>ABPerson</tt>
     * property in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABCalendarURIsProperty = 21;

    /**
     * The index of the <tt>kABAddressProperty</tt> <tt>ABPerson</tt>
     * property in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABAddressProperty = 22;

    /**
     * The index of the <tt>kABOtherDatesProperty</tt> <tt>ABPerson</tt>
     * property in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABOtherDatesProperty = 23;

    /**
     * The index of the <tt>kABRelatedNamesProperty</tt> <tt>ABPerson</tt>
     * property in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABRelatedNamesProperty = 24;

    /**
     * The index of the <tt>kABDepartmentProperty</tt> <tt>ABPerson</tt>
     * property in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABDepartmentProperty = 25;

    /**
     * The index of the <tt>kABNoteProperty</tt> <tt>ABPerson</tt>
     * property in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABNoteProperty = 26;

    /**
     * The index of the <tt>kABSocialProfileProperty</tt> <tt>ABPerson</tt>
     * property in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABSocialProfileProperty = 27;

    /**
     * The index of the <tt>kABTitleProperty</tt> <tt>ABPerson</tt>
     * property in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABTitleProperty = 28;

    /**
     * The index of the <tt>kABSuffixProperty</tt> <tt>ABPerson</tt>
     * property in {@link #ABPERSON_PROPERTIES}.
     */
    private static final int kABSuffixProperty = 29;

    /**
     * The regex which matches the superfluous parts of an <tt>ABMultiValue</tt>
     * label.
     */
    private static final Pattern LABEL_PATTERN
        = Pattern.compile(
            "kAB|Email|Phone|Label|(\\p{Punct}*)",
            Pattern.CASE_INSENSITIVE);

    static
    {
        System.loadLibrary("jmacosxaddrbook");
    }

    /**
     * Initializes a new <tt>MacOSXAddrBookContactQuery</tt> which is to perform
     * a specific <tt>query</tt> in the Address Book of Mac OS X on behalf of a
     * specific <tt>MacOSXAddrBookContactSourceService</tt>.
     *
     * @param contactSource the <tt>MacOSXAddrBookContactSourceService</tt>
     * which is to perform the new <tt>ContactQuery</tt> instance
     * @param query the <tt>Pattern</tt> for which <tt>contactSource</tt> i.e.
     * the Address Book of Mac OS X is being queried
     */
    public MacOSXAddrBookContactQuery(
            MacOSXAddrBookContactSourceService contactSource,
            Pattern query)
    {
        super(contactSource, query);
    }

    /**
     * Gets the <tt>imageData</tt> of a specific <tt>ABPerson</tt> instance.
     *
     * @param person the pointer to the <tt>ABPerson</tt> instance to get the
     * <tt>imageData</tt> of
     * @return the <tt>imageData</tt> of the specified <tt>ABPerson</tt>
     * instance
     */
    public static native byte[] ABPerson_imageData(long person);

    /**
     * Gets the values of a specific set of <tt>ABRecord</tt> properties for a
     * specific <tt>ABRecord</tt> instance.
     *
     * @param record the pointer to the <tt>ABRecord</tt> to get the property
     * values of
     * @param properties the set of <tt>ABRecord</tt> properties to get the
     * values of
     * @return the values of the specified set of <tt>ABRecord</tt> properties
     * for the specified <tt>ABRecord</tt> instance
     */
    public static native Object[] ABRecord_valuesForProperties(
            long record,
            long[] properties);

    /**
     * Returns the unique id of a record.
     * @param record the record which id is retrieved.
     * @return the record id.
     */
    public static native String ABRecord_uniqueId(long record);

    /**
     * Sets property for the supplied person id.
     * @param id the person id
     * @param property the property to use.
     * @param subPropety any sub property if available.
     * @param value the value to set.
     * @return whether the result was successfully added.
     */
    public static native boolean setProperty(
        String id, long property, String subPropety, Object value);

    /**
     * Remove a property.
     * @param id the person id.
     * @param property the property.
     * @return whether the result was successfully removed.
     */
    public static native boolean removeProperty(String id, long property);

    /**
     * Initializes a new <tt>ContactDetail</tt> instance which is to reperesent
     * a specific contact address that is the value of a specific
     * <tt>ABPerson</tt> property and, optionally, has a specific label.
     *
     * @param property the index in {@link #ABPERSON_PROPERTIES} of the
     * <tt>ABPerson</tt> property to be represented by <tt>ContactDetail</tt>
     * @param contactAddress the contact address to be represented by the new
     * <tt>ContactDetail</tt> instance
     * @param label an optional label to be added to the set of labels, if any,
     * determined by <tt>property</tt>
     * @return a new <tt>ContactDetail</tt> instance which represents the
     * specified <tt>contactAddress</tt>
     */
    private ContactDetail createContactDetail(
            int property,
            String contactAddress,
            Object label,
            String additionalProperty)
    {
        ContactDetail.Category c;
        ContactDetail.SubCategory sc = null;

        switch (property)
        {
        case kABEmailProperty:
            c = ContactDetail.Category.Email;
            break;
        case kABPhoneProperty:
            c = ContactDetail.Category.Phone;
            break;
        case kABAIMInstantProperty:
            sc = ContactDetail.SubCategory.AIM;
            c = ContactDetail.Category.InstantMessaging;
            break;
        case kABICQInstantProperty:
            sc = ContactDetail.SubCategory.ICQ;
            c = ContactDetail.Category.InstantMessaging;
            break;
        case kABJabberInstantProperty:
            sc = ContactDetail.SubCategory.Jabber;
            c = ContactDetail.Category.InstantMessaging;
            break;
        case kABMSNInstantProperty:
            sc = ContactDetail.SubCategory.MSN;
            c = ContactDetail.Category.InstantMessaging;
            break;
        case kABYahooInstantProperty:
            sc = ContactDetail.SubCategory.Yahoo;
            c = ContactDetail.Category.InstantMessaging;
            break;
        case kABMaidenNameProperty:
        case kABFirstNameProperty:
            sc = ContactDetail.SubCategory.Name;
            c = ContactDetail.Category.Personal;
            break;
        case kABFirstNamePhoneticProperty:
            sc = ContactDetail.SubCategory.Name;
            c = ContactDetail.Category.Personal;
            break;
        case kABLastNameProperty:
            sc = ContactDetail.SubCategory.LastName;
            c = ContactDetail.Category.Personal;
            break;
        case kABLastNamePhoneticProperty:
            sc = ContactDetail.SubCategory.LastName;
            c = ContactDetail.Category.Personal;
            break;
        case kABMiddleNameProperty:
        case kABMiddleNamePhoneticProperty:
        case kABNicknameProperty:
            sc = ContactDetail.SubCategory.Nickname;
            c = ContactDetail.Category.Personal;
            break;
        case kABBirthdayProperty:
        case kABURLsProperty:
        case kABHomePageProperty:
            sc = ContactDetail.SubCategory.HomePage;
            c = ContactDetail.Category.Personal;
            break;
        case kABOtherDatesProperty:
        case kABRelatedNamesProperty:
        case kABNoteProperty:
        case kABSocialProfileProperty:
        case kABTitleProperty:
        case kABSuffixProperty:
            c = ContactDetail.Category.Personal;
            break;
        case kABOrganizationProperty:
        case kABJobTitleProperty:
            sc = ContactDetail.SubCategory.JobTitle;
            c = ContactDetail.Category.Organization;
            break;
        case kABDepartmentProperty:
            c = ContactDetail.Category.Organization;
            break;
        case kABAddressProperty:
            c = ContactDetail.Category.Address;
            break;
        default:
            c = null;
            break;
        }

        if (sc == null)
        {
            if (label == null)
                sc = null;
            else
            {
                sc = getSubCategoryFromLabel(label);
            }
        }

        return new AddressBookContactDetail(
            property,
            contactAddress,
            c,
            new ContactDetail.SubCategory[] { sc },
            additionalProperty);
    }

    /**
     * Returns the ContactDetail.SubCategory corresponding to the given label.
     *
     * @param label the label to match to a <tt>ContactDetail.SubDirectory</tt>
     * @return the <tt>ContactDetail.SubDirectory</tt> corresponding to the
     * given label
     */
    private ContactDetail.SubCategory getSubCategoryFromLabel(Object label)
    {
        String labelString
            = LABEL_PATTERN.matcher((String) label).replaceAll("").trim();

        if (labelString.length() < 1)
            return null;

        ContactDetail.SubCategory subCategory = null;

        if (labelString.equalsIgnoreCase("home"))
            subCategory = ContactDetail.SubCategory.Home;
        else if (labelString.equalsIgnoreCase("work"))
            subCategory = ContactDetail.SubCategory.Work;
        else if (labelString.equalsIgnoreCase("other"))
            subCategory = ContactDetail.SubCategory.Other;
        else if (labelString.equalsIgnoreCase("mobile"))
            subCategory = ContactDetail.SubCategory.Mobile;
        else if (labelString.equalsIgnoreCase("homepage"))
            subCategory = ContactDetail.SubCategory.HomePage;
        else if (labelString.equalsIgnoreCase("street"))
            subCategory = ContactDetail.SubCategory.Street;
        else if (labelString.equalsIgnoreCase("ZIP"))
            subCategory = ContactDetail.SubCategory.PostalCode;
        else if (labelString.equalsIgnoreCase("country"))
            subCategory = ContactDetail.SubCategory.Country;
        else if (labelString.equalsIgnoreCase("city"))
            subCategory = ContactDetail.SubCategory.City;
        else if (labelString.equalsIgnoreCase("InstantMessageUsername"))
            subCategory = ContactDetail.SubCategory.Nickname;

        return subCategory;
    }

    /**
     * Calls back to a specific <tt>PtrCallback</tt> for each <tt>ABPerson</tt>
     * found in the Address Book of Mac OS X which matches a specific
     * <tt>String</tt> query.
     *
     * @param query the <tt>String</tt> for which the Address Book of Mac OS X
     * is to be queried. <b>Warning</b>: Ignored at the time of this writing.
     * @param callback the <tt>PtrCallback</tt> to be notified about the
     * matching <tt>ABPerson</tt>s
     */
    private static native void foreachPerson(
            String query,
            PtrCallback callback);

    /**
     * Gets the <tt>contactDetails</tt> to be set on a <tt>SourceContact</tt>
     * which is to represent an <tt>ABPerson</tt> specified by the values of its
     * {@link #ABPERSON_PROPERTIES}.
     *
     * @param values the values of the <tt>ABPERSON_PROPERTIES</tt> which
     * represent the <tt>ABPerson</tt> to get the <tt>contactDetails</tt> of
     * @return the <tt>contactDetails</tt> to be set on a <tt>SourceContact</tt>
     * which is to represent the <tt>ABPerson</tt> specified by <tt>values</tt>
     */
    private List<ContactDetail> getContactDetails(Object[] values)
    {
        List<ContactDetail> contactDetails = new LinkedList<ContactDetail>();

        for (int i = 0; i < ABPERSON_PROPERTIES.length; i++)
        {
            int property = i;
            Object value = values[property];

            if (value instanceof String)
            {
                String stringValue = (String) value;

                if (stringValue.length() != 0)
                {
                    if (kABPhoneProperty == property)
                        stringValue
                            = PhoneNumberI18nService.normalize(stringValue);

                    contactDetails.add(
                            setCapabilities(
                                    createContactDetail(
                                            property,
                                            stringValue,
                                            null,
                                            null),
                                    property));
                }
            }
            else if (value instanceof Object[])
            {
                parseMultiDetails(contactDetails,
                                  (Object[]) value,
                                  property,
                                  null);
            }
        }
        return contactDetails;
    }

    /**
     * Parses the multi value data resulting it in contact details.
     * @param contactDetails the result list
     * @param multiValue the values to parse.
     * @param property the current property being parsed.
     */
    private void parseMultiDetails(
        List<ContactDetail> contactDetails,
        Object[] multiValue,
        int property,
        String label)
    {
        if(multiValue == null)
            return;

        for (int multiValueIndex = 0;
                multiValueIndex < multiValue.length;
                multiValueIndex += 2)
        {
            Object subValue = multiValue[multiValueIndex];

            if (subValue instanceof String)
            {
                String stringSubValue = (String) subValue;

                if (stringSubValue.length() != 0)
                {
                    if (kABPhoneProperty == property)
                    {
                        stringSubValue = PhoneNumberI18nService
                            .normalize(stringSubValue);
                    }

                    Object l = multiValue[multiValueIndex + 1];

                    contactDetails.add(
                            setCapabilities(
                                    createContactDetail(
                                        property,
                                        stringSubValue,
                                        l,
                                        label),
                                    property));
                }
            }
            else if (subValue instanceof Object[])
            {
                String l = null;

                Object lObject = multiValue[multiValueIndex + 1];
                if(lObject instanceof String)
                    l = (String)lObject;

                parseMultiDetails(contactDetails,
                                  (Object[]) subValue,
                                  property,
                                  l);
            }
        }
    }

    /**
     * Gets the <tt>displayName</tt> to be set on a <tt>SourceContact</tt>
     * which is to represent an <tt>ABPerson</tt> specified by the values of its
     * {@link #ABPERSON_PROPERTIES}.
     *
     * @param values the values of the <tt>ABPERSON_PROPERTIES</tt> which
     * represent the <tt>ABPerson</tt> to get the <tt>displayName</tt> of
     * @return the <tt>displayName</tt> to be set on a <tt>SourceContact</tt>
     * which is to represent the <tt>ABPerson</tt> specified by <tt>values</tt>
     */
    static String getDisplayName(Object[] values)
    {
        long personFlags
            = (values[kABPersonFlags] instanceof Long)
                ? ((Long) values[kABPersonFlags]).longValue()
                : 0;
        String displayName;

        if ((personFlags & kABShowAsMask) == kABShowAsCompany)
        {
            displayName
                = (values[kABOrganizationProperty] instanceof String)
                    ? (String) values[kABOrganizationProperty]
                    : "";
            if (displayName.length() != 0)
                return displayName;
        }

        displayName
            = (values[kABNicknameProperty] instanceof String)
                ? (String) values[kABNicknameProperty]
                : "";
        if (displayName.length() != 0)
            return displayName;

        String firstName
            = (values[kABFirstNameProperty] instanceof String)
                ? (String) values[kABFirstNameProperty]
                : "";

        if ((firstName.length() == 0)
                && (values[kABFirstNamePhoneticProperty] instanceof String))
        {
            firstName = (String) values[kABFirstNamePhoneticProperty];
        }

        String lastName
            = (values[kABLastNameProperty] instanceof String)
                ? (String) values[kABLastNameProperty]
                : "";

        if ((lastName.length() == 0)
                && (values[kABLastNamePhoneticProperty] instanceof String))
            lastName = (String) values[kABLastNamePhoneticProperty];
        if ((lastName.length() == 0)
                && (values[kABMiddleNameProperty] instanceof String))
            lastName = (String) values[kABMiddleNameProperty];
        if ((lastName.length() == 0)
                && (values[kABMiddleNamePhoneticProperty] instanceof String))
            lastName = (String) values[kABMiddleNamePhoneticProperty];

        if (firstName.length() == 0)
            displayName = lastName;
        else
        {
            displayName
                = (lastName.length() == 0)
                    ? firstName
                    : (firstName + " " + lastName);
        }
        if (displayName.length() != 0)
            return displayName;

        for (int i = 0; i < ABPERSON_PROPERTIES.length; i++)
        {
            Object value = values[i];

            if (value instanceof String)
            {
                String stringValue = (String) value;

                if (stringValue.length() != 0)
                {
                    displayName = stringValue;
                    break;
                }
            }
            else if (value instanceof Object[])
            {
                Object[] multiValue = (Object[]) value;

                for (int multiValueIndex = 0;
                        multiValueIndex < multiValue.length;
                        multiValueIndex += 2)
                {
                    Object subValue = multiValue[multiValueIndex];

                    if (subValue instanceof String)
                    {
                        String stringSubValue = (String) subValue;

                        if (stringSubValue.length() != 0)
                        {
                            displayName = stringSubValue;
                            break;
                        }
                    }
                }
            }
        }
        return displayName;
    }

    /**
     * Gets the value of the <tt>kABAIMInstantProperty</tt> constant.
     *
     * @return the value of the <tt>kABAIMInstantProperty</tt> constant
     */
    private static native long kABAIMInstantProperty();

    /**
     * Gets the value of the <tt>kABEmailProperty</tt> constant.
     *
     * @return the value of the <tt>kABEmailProperty</tt> constant
     */
    private static native long kABEmailProperty();

    /**
     * Gets the value of the <tt>kABFirstNameProperty</tt> constant.
     *
     * @return the value of the <tt>kABFirstNameProperty</tt> constant
     */
    private static native long kABFirstNameProperty();

    /**
     * Gets the value of the <tt>kABFirstNamePhoneticProperty</tt> constant.
     *
     * @return the value of the <tt>kABFirstNamePhoneticProperty</tt> constant
     */
    private static native long kABFirstNamePhoneticProperty();

    /**
     * Gets the value of the <tt>kABICQInstantProperty</tt> constant.
     *
     * @return the value of the <tt>kABICQInstantProperty</tt> constant
     */
    private static native long kABICQInstantProperty();

    /**
     * Gets the value of the <tt>kABJabberInstantProperty</tt> constant.
     *
     * @return the value of the <tt>kABJabberInstantProperty</tt> constant
     */
    private static native long kABJabberInstantProperty();

    /**
     * Gets the value of the <tt>kABLastNameProperty</tt> constant.
     *
     * @return the value of the <tt>kABLastNameProperty</tt> constant
     */
    private static native long kABLastNameProperty();

    /**
     * Gets the value of the <tt>kABLastNamePhoneticProperty</tt> constant.
     *
     * @return the value of the <tt>kABLastNamePhoneticProperty</tt> constant
     */
    private static native long kABLastNamePhoneticProperty();

    /**
     * Gets the value of the <tt>kABMiddleNameProperty</tt> constant.
     *
     * @return the value of the <tt>kABMiddleNameProperty</tt> constant
     */
    private static native long kABMiddleNameProperty();

    /**
     * Gets the value of the <tt>kABMiddleNamePhoneticProperty</tt> constant.
     *
     * @return the value of the <tt>kABMiddleNamePhoneticProperty</tt> constant
     */
    private static native long kABMiddleNamePhoneticProperty();

    /**
     * Gets the value of the <tt>kABMSNInstantProperty</tt> constant.
     *
     * @return the value of the <tt>kABMSNInstantProperty</tt> constant
     */
    private static native long kABMSNInstantProperty();

    /**
     * Gets the value of the <tt>kABNicknameProperty</tt> constant.
     *
     * @return the value of the <tt>kABNicknameProperty</tt> constant
     */
    private static native long kABNicknameProperty();

    /**
     * Gets the value of the <tt>kABOrganizationProperty</tt> constant.
     *
     * @return the value of the <tt>kABOrganizationProperty</tt> constant
     */
    private static native long kABOrganizationProperty();

    /**
     * Gets the value of the <tt>kABPersonFlags</tt> constant.
     *
     * @return the value of the <tt>kABPersonFlags</tt> constant
     */
    private static native long kABPersonFlags();

    /**
     * Gets the value of the <tt>kABPhoneProperty</tt> constant.
     *
     * @return the value of the <tt>kABPhoneProperty</tt> constant
     */
    private static native long kABPhoneProperty();

    /**
     * Gets the value of the <tt>kABYahooInstantProperty</tt> constant.
     *
     * @return the value of the <tt>kABYahooInstantProperty</tt> constant
     */
    private static native long kABYahooInstantProperty();

    /**
     * Gets the value of the <tt>kABMaidenNameProperty</tt> constant.
     *
     * @return the value of the <tt>kABMaidenNameProperty</tt> constant
     */
    private static native long kABMaidenNameProperty();

    /**
     * Gets the value of the <tt>kABBirthdayProperty</tt> constant.
     *
     * @return the value of the <tt>kABBirthdayProperty</tt> constant
     */
    private static native long kABBirthdayProperty();

    /**
     * Gets the value of the <tt>kABJobTitleProperty</tt> constant.
     *
     * @return the value of the <tt>kABJobTitleProperty</tt> constant
     */
    private static native long kABJobTitleProperty();

    /**
     * Gets the value of the <tt>kABHomePageProperty</tt> constant.
     *
     * @return the value of the <tt>kABHomePageProperty</tt> constant
     */
    private static native long kABHomePageProperty();

    /**
     * Gets the value of the <tt>kABURLsProperty</tt> constant.
     *
     * @return the value of the <tt>kABURLsProperty</tt> constant
     */
    private static native long kABURLsProperty();

    /**
     * Gets the value of the <tt>kABCalendarURIsProperty</tt> constant.
     *
     * @return the value of the <tt>kABCalendarURIsProperty</tt> constant
     */
    private static native long kABCalendarURIsProperty();

    /**
     * Gets the value of the <tt>kABAddressProperty</tt> constant.
     *
     * @return the value of the <tt>kABAddressProperty</tt> constant
     */
    private static native long kABAddressProperty();

    /**
     * Gets the value of the <tt>kABOtherDatesProperty</tt> constant.
     *
     * @return the value of the <tt>kABOtherDatesProperty</tt> constant
     */
    private static native long kABOtherDatesProperty();

    /**
     * Gets the value of the <tt>kABRelatedNamesProperty</tt> constant.
     *
     * @return the value of the <tt>kABRelatedNamesProperty</tt> constant
     */
    private static native long kABRelatedNamesProperty();

    /**
     * Gets the value of the <tt>kABDepartmentProperty</tt> constant.
     *
     * @return the value of the <tt>kABDepartmentProperty</tt> constant
     */
    private static native long kABDepartmentProperty();

    /**
     * Gets the value of the <tt>kABInstantMessageProperty</tt> constant.
     *
     * @return the value of the <tt>kABInstantMessageProperty</tt> constant
     */
    private static native long kABInstantMessageProperty();

    /**
     * Gets the value of the <tt>kABNoteProperty</tt> constant.
     *
     * @return the value of the <tt>kABNoteProperty</tt> constant
     */
    private static native long kABNoteProperty();

    /**
     * Gets the value of the <tt>kABSocialProfileProperty</tt> constant.
     *
     * @return the value of the <tt>kABSocialProfileProperty</tt> constant
     */
    private static native long kABSocialProfileProperty();

    /**
     * Gets the value of the <tt>kABTitleProperty</tt> constant.
     *
     * @return the value of the <tt>kABTitleProperty</tt> constant
     */
    private static native long kABTitleProperty();

    /**
     * Gets the value of the <tt>kABSuffixProperty</tt> constant.
     *
     * @return the value of the <tt>kABSuffixProperty</tt> constant
     */
    private static native long kABSuffixProperty();

    private static native String kABEmailWorkLabel();
    private static native String kABEmailHomeLabel();
    private static native String kABAddressHomeLabel();
    private static native String kABAddressWorkLabel();
    private static native String kABPhoneWorkLabel();
    private static native String kABPhoneHomeLabel();
    private static native String kABPhoneMobileLabel();
    private static native String kABPhoneMainLabel();
    private static native String kABPhoneWorkFAXLabel();
    private static native String kABHomeLabel();
    private static native String kABWorkLabel();
    private static native String kABOtherLabel();
    private static native String kABAddressStreetKey();
    private static native String kABAddressCityKey();
    private static native String kABAddressZIPKey();
    private static native String kABAddressCountryKey();


    /**
     * Determines whether a specific <tt>ABPerson</tt> property with a specific
     * <tt>value</tt> matches the {@link #query} of this
     * <tt>AsyncContactQuery</tt>.
     *
     * @param property the <tt>ABPerson</tt> property to check
     * @param value the value of the <tt>property</tt> to check
     * @return <tt>true</tt> if the specified <tt>value</tt> of the specified
     * <tt>property</tt> matches the <tt>query</tt> of this
     * <tt>AsyncContactQuery</tt>; otherwise, <tt>false</tt>
     */
    private boolean matches(int property, String value)
    {
        return
            query.matcher(value).find()
                || ((kABPhoneProperty == property) && phoneNumberMatches(value));
    }

    /**
     * Determines whether an <tt>ABPerson</tt> represented by the values of its
     * {@link #ABPERSON_PROPERTIES} matches {@link #query}.
     *
     * @param values the values of the <tt>ABPERSON_PROPERTIES</tt> which
     * represent the <tt>ABPerson</tt> to be determined whether it matches
     * <tt>query</tt>
     * @return <tt>true</tt> if the <tt>ABPerson</tt> represented by the
     * specified <tt>values</tt> matches <tt>query</tt>; otherwise,
     * <tt>false</tt>
     */
    private boolean matches(Object[] values)
    {
        int property = 0;

        for (Object value : values)
        {
            if (value instanceof String)
            {
                if (matches(property, (String) value))
                    return true;
            }
            else if (value instanceof Object[])
            {
                Object[] multiValue = (Object[]) value;

                for (int multiValueIndex = 0;
                        multiValueIndex < multiValue.length;
                        multiValueIndex += 2)
                {
                    Object subValue = multiValue[multiValueIndex];
                    if ((subValue instanceof String)
                            && matches(property, (String) subValue))
                        return true;
                }
            }
            property++;
        }
        return false;
    }

    /**
     * Notifies this <tt>MacOSXAddrBookContactQuery</tt> about a specific
     * <tt>ABPerson</tt>.
     *
     * @param person a pointer to the <tt>ABPerson</tt> instance to notify about
     * @return <tt>true</tt> if this <tt>MacOSXAddrBookContactQuery</tt> is to
     * continue being called; otherwise, <tt>false</tt>
     */
    private boolean onPerson(long person)
    {
        Object[] values
            = ABRecord_valuesForProperties(person, ABPERSON_PROPERTIES);
        final String id = ABRecord_uniqueId(person);

        String displayName = getDisplayName(values);
        if ((displayName.length() != 0)
            && (query.matcher(displayName).find() || matches(values)))
        {
            List<ContactDetail> contactDetails = getContactDetails(values);

            if (!contactDetails.isEmpty())
            {
                final AddressBookSourceContact sourceContact
                    = new AddressBookSourceContact(
                            getContactSource(),
                            displayName,
                            contactDetails);
                sourceContact.setData(
                    SOURCE_CONTACT_ID_DATA_KEY,
                    id);

                for(ContactDetail cd : contactDetails)
                {
                    if(cd instanceof AddressBookContactDetail)
                        ((AddressBookContactDetail)cd).setId(id);
                }

                try
                {
                    byte[] image = ABPerson_imageData(person);

                    if (image != null)
                        sourceContact.setImage(image);
                }
                catch (OutOfMemoryError oome)
                {
                    // Ignore it, the image is not vital.
                }

                addQueryResult(sourceContact);
            }
        }
        return (getStatus() == QUERY_IN_PROGRESS);
    }

    /**
     * Performs this <tt>AsyncContactQuery</tt> in a background <tt>Thread</tt>.
     *
     * @see AsyncContactQuery#run()
     */
    protected void run()
    {
        foreachPerson(
            query.toString(),
            new PtrCallback()
            {
                public boolean callback(long person)
                {
                    return onPerson(person);
                }
            });
    }

    /**
     * Sets the capabilities of a specific <tt>ContactDetail</tt> (e.g.
     * <tt>supportedOpSets</tt>) depending on the <tt>ABPerson</tt> property
     * that it stands for.
     *
     * @param contactDetail the <tt>ContactDetail</tt> to set the capabilities
     * of
     * @param property the index in {@link #ABPERSON_PROPERTIES} of the
     * <tt>ABPerson</tt> property represented by <tt>ContactDetail</tt>
     * @return <tt>contactDetail</tt>
     */
    private ContactDetail setCapabilities(
            ContactDetail contactDetail,
            int property)
    {
        List<Class<? extends OperationSet>> supportedOpSets
            = new LinkedList<Class<? extends OperationSet>>();
        Map<Class<? extends OperationSet>, String> preferredProtocols
            = new HashMap<Class<? extends OperationSet>, String>();

        // can be added as contacts
        supportedOpSets.add(OperationSetPersistentPresence.class);

        switch (property)
        {
        case kABAIMInstantProperty:
            supportedOpSets.add(OperationSetBasicInstantMessaging.class);
            preferredProtocols.put(
                    OperationSetBasicInstantMessaging.class,
                    ProtocolNames.AIM);
            break;
        case kABEmailProperty:
            break;
        case kABICQInstantProperty:
            supportedOpSets.add(OperationSetBasicInstantMessaging.class);
            preferredProtocols.put(
                    OperationSetBasicInstantMessaging.class,
                    ProtocolNames.ICQ);
            break;
        case kABJabberInstantProperty:
            supportedOpSets.add(OperationSetBasicInstantMessaging.class);
            preferredProtocols.put(
                    OperationSetBasicInstantMessaging.class,
                    ProtocolNames.JABBER);
            supportedOpSets.add(OperationSetBasicTelephony.class);
            preferredProtocols.put(
                    OperationSetBasicTelephony.class,
                    ProtocolNames.JABBER);
            break;
        case kABPhoneProperty:
            supportedOpSets.add(OperationSetBasicTelephony.class);
            break;
        case kABMSNInstantProperty:
            supportedOpSets.add(OperationSetBasicInstantMessaging.class);
            preferredProtocols.put(
                    OperationSetBasicInstantMessaging.class,
                    ProtocolNames.MSN);
            break;
        case kABYahooInstantProperty:
            supportedOpSets.add(OperationSetBasicInstantMessaging.class);
            preferredProtocols.put(
                    OperationSetBasicInstantMessaging.class,
                    ProtocolNames.YAHOO);
            break;
        default:
            break;
        }
        contactDetail.setSupportedOpSets(supportedOpSets);
        if (!preferredProtocols.isEmpty())
            contactDetail.setPreferredProtocols(preferredProtocols);

        return contactDetail;
    }

    /**
     * Callback method when receiving notifications for inserted items.
     */
    public void inserted(long person)
    {
        onPerson(person);
    }

    /**
     * Callback method when receiving notifications for updated items.
     */
    public void updated(long person)
    {
        SourceContact sourceContact =
            findSourceContactByID(ABRecord_uniqueId(person));
        if(sourceContact != null
            && sourceContact instanceof AddressBookSourceContact)
        {
            // let's update the the details
            Object[] values
                = ABRecord_valuesForProperties(person, ABPERSON_PROPERTIES);

            AddressBookSourceContact editableSourceContact
                = (AddressBookSourceContact)sourceContact;

            List<ContactDetail> contactDetails = getContactDetails(values);
            editableSourceContact.setDetails(contactDetails);

            fireContactChanged(sourceContact);
        }
    }

    /**
     * Callback method when receiving notifications for deleted items.
     */
    public void deleted(String id)
    {
        SourceContact sourceContact = findSourceContactByID(id);

        if(sourceContact != null)
            fireContactRemoved(sourceContact);
    }

    /**
     * Whether the value for the category are multiline.
     * @param category
     * @return
     */
    private boolean isMultiline(AddressBookContactDetail.Category category)
    {
        switch(category)
        {
            case Personal:
                return false;
            case Organization:
                return false;
            case Email:
                return true;
            case InstantMessaging:
                return true;
            case Phone:
                return true;
            case Address:
                return true;
            default:
                return false;
        }
    }

    /**
     * Find the property from category and subcategories.
     *
     * @param category
     * @param subCategories
     * @return
     */
    public static int getProperty(
        AddressBookContactDetail.Category category,
        Collection<AddressBookContactDetail.SubCategory> subCategories)
    {
        switch(category)
        {
            case Personal:
                if(subCategories.contains(ContactDetail.SubCategory.Name))
                    return kABFirstNameProperty;
                else if(subCategories.contains(ContactDetail.SubCategory.LastName))
                    return kABLastNameProperty;
                else if(subCategories.contains(ContactDetail.SubCategory.Nickname))
                    return kABLastNameProperty;
                else if(subCategories.contains(ContactDetail.SubCategory.HomePage))
                    return kABHomePageProperty;
                break;
            case Organization:
                if(subCategories.contains(ContactDetail.SubCategory.JobTitle))
                    return kABJobTitleProperty;
                else
                    return kABDepartmentProperty;
            case Email:
                return kABEmailProperty;
            case InstantMessaging:
                if(subCategories.contains(ContactDetail.SubCategory.AIM))
                    return kABAIMInstantProperty;
                else if(subCategories.contains(ContactDetail.SubCategory.ICQ))
                    return kABICQInstantProperty;
                else if(subCategories.contains(ContactDetail.SubCategory.MSN))
                    return kABMSNInstantProperty;
                else if(subCategories.contains(
                            ContactDetail.SubCategory.Jabber))
                    return kABJabberInstantProperty;
                else if(subCategories.contains(
                            ContactDetail.SubCategory.Yahoo))
                    return kABYahooInstantProperty;
                break;
            case Phone:
                return kABPhoneProperty;
            case Address:
                return kABAddressProperty;
            default: return -1;
        }

        return -1;
    }

    /**
     * Finds the label from category and sub categories.
     * @param subCategory
     * @return
     */
    public static String getLabel(
        int property,
        AddressBookContactDetail.SubCategory subCategory,
        String subProperty)
    {
        switch(property)
        {
            case kABEmailProperty:
                if(subCategory == ContactDetail.SubCategory.Home)
                    return kABEmailHomeLabel();
                if(subCategory == ContactDetail.SubCategory.Work)
                    return kABEmailWorkLabel();
                break;
            case kABICQInstantProperty:
            case kABAIMInstantProperty:
            case kABYahooInstantProperty:
            case kABMSNInstantProperty:
            case kABJabberInstantProperty:
                return subProperty;
            case kABPhoneProperty:
                if(subCategory == ContactDetail.SubCategory.Home)
                    return kABPhoneHomeLabel();
                if(subCategory == ContactDetail.SubCategory.Work)
                    return kABPhoneWorkLabel();
                if(subCategory == ContactDetail.SubCategory.Fax)
                    return kABPhoneWorkFAXLabel();
                break;
            case kABAddressProperty:
                if(subCategory == ContactDetail.SubCategory.Street)
                    return kABAddressStreetKey();
                if(subCategory == ContactDetail.SubCategory.City)
                    return kABAddressCityKey();
                if(subCategory == ContactDetail.SubCategory.Country)
                    return kABAddressCountryKey();
                if(subCategory == ContactDetail.SubCategory.PostalCode)
                    return kABAddressZIPKey();
                break;
            default: return null;
        }

        return null;
    }

    /**
     * Our editable source contact, we store changes in the addressbook.
     */
    private class AddressBookSourceContact
        extends GenericSourceContact
        implements EditableSourceContact
    {
        /**
         * Initializes a new <tt>AddrBookSourceContact</tt> instance.
         *
         * @param contactSource the <tt>ContactSourceService</tt> which is creating
         * the new instance
         * @param displayName the display name of the new instance
         * @param contactDetails the <tt>ContactDetail</tt>s of the new instance
         */
        public AddressBookSourceContact(
                ContactSourceService contactSource,
                String displayName,
                List<ContactDetail> contactDetails)
        {
            super(contactSource, displayName, contactDetails);

            // let's save the parent so we can reuse it later when editing
            // the detail
            for(ContactDetail cd : contactDetails)
            {
                if(cd instanceof AddressBookContactDetail)
                {
                    ((AddressBookContactDetail)cd).setParent(this);
                }
            }
        }

        /**
         * Adds a contact detail to the list of contact details.
         *
         * @param detail the <tt>ContactDetail</tt> to add
         */
        @SuppressWarnings("unchecked")
        public void addContactDetail(ContactDetail detail)
        {
            String id = (String)getData(SOURCE_CONTACT_ID_DATA_KEY);

            if(id == null)
            {
                logger.warn("No id or wrong ContactDetail");
                return;
            }

            int property = getProperty(
                detail.getCategory(), detail.getSubCategories());

            if(isMultiline(detail.getCategory()))
            {
                String subProperty = null;

                if(detail instanceof AddressBookContactDetail)
                {
                    subProperty = ((AddressBookContactDetail)detail)
                        .getSubPropertyLabel();
                }

                List<ContactDetail> details =
                    getContactDetails(detail.getCategory());

                boolean isIM = (property == kABICQInstantProperty
                                || property == kABAIMInstantProperty
                                || property == kABYahooInstantProperty
                                || property == kABMSNInstantProperty
                                || property == kABJabberInstantProperty);

                // first add existing one
                ArrayList values = new ArrayList();

                for(ContactDetail cd : details)
                {
                    String det = cd.getDetail();

                    for(ContactDetail.SubCategory sub : cd.getSubCategories())
                    {
                        // if its an im property check also if the detail
                        // is the same subcategory (which is icq, yahoo, ...)
                        if(isIM && !detail.getSubCategories().contains(sub))
                            continue;

                        String label = getLabel(property, sub, subProperty);
                        if(label != null)
                        {
                            values.add(det);
                            values.add(label);
                        }
                        else
                            logger.warn("Missing label fo prop:" + property
                                + " and sub:" + sub);
                    }
                }

                // now the new value to add
                for(ContactDetail.SubCategory sub : detail.getSubCategories())
                {
                    String label = getLabel(property, sub, subProperty);
                    if(label != null)
                    {
                        values.add(detail.getDetail());
                        values.add(label);
                    }
                    else
                        logger.warn("Missing label fo prop:" + property
                            + " and sub:" + sub);
                }

                setProperty(id, ABPERSON_PROPERTIES[property], subProperty,
                    values.toArray(new Object[values.size()]));

            }
            else
            {
                setProperty(id, ABPERSON_PROPERTIES[property], null,
                    detail.getDetail());
            }

            // make sure we add AddressBookContactDetail
            Collection<ContactDetail.SubCategory> subCategories
                = detail.getSubCategories();
            contactDetails.add(
                new AddressBookContactDetail(
                    property,
                    detail.getDetail(),
                    detail.getCategory(),
                    subCategories.toArray(
                        new ContactDetail.SubCategory[
                            subCategories.size()]),
                    null));
        }

        /**
         * Removes the given <tt>ContactDetail</tt> from the list of details for
         * this <tt>SourceContact</tt>.
         *
         * @param detail the <tt>ContactDetail</tt> to remove
         */
        public void removeContactDetail(ContactDetail detail)
        {
            //remove the detail from the addressbook
            String id = (String)getData(SOURCE_CONTACT_ID_DATA_KEY);
            if(id != null && detail instanceof AddressBookContactDetail)
            {
                removeProperty(id,
                    ((AddressBookContactDetail)detail).property);
            }
            else
                logger.warn("No id or wrong ContactDetail");

            contactDetails.remove(detail);
        }

        /**
         * Changes the details list with the supplied one.
         * @param details the details.
         */
        public void setDetails(List<ContactDetail> details)
        {
            contactDetails.clear();
            contactDetails.addAll(details);
        }
    }

    /**
     * The editable detail, change get changed and in addressbook.
     */
    private class AddressBookContactDetail
        extends EditableContactDetail
    {
        /**
         * The property index for this detail.
         */
        private final int property;

        /**
         * The id of the detail.
         */
        private String id;

        /**
         * The parent contact source.
         */
        private AddressBookSourceContact parent;

        private String subPropertyLabel;

        /**
         * Initializes a new <tt>ContactDetail</tt> instance which is to represent a
         * specific contact address and which is to be optionally labeled with a
         * specific set of labels.
         *
         * @param contactDetailValue the contact detail value to be represented by
         * the new <tt>ContactDetail</tt> instance
         * @param category
         * @param subCategories the set of sub categories with which the new
         * <tt>ContactDetail</tt> instance is to be labeled.
         */
        public AddressBookContactDetail(
                int property,
                String contactDetailValue,
                ContactDetail.Category category,
                ContactDetail.SubCategory[] subCategories,
                String subPropertyLabel)
        {
            super(contactDetailValue, category, subCategories);
            this.property = property;
            this.subPropertyLabel = subPropertyLabel;
        }

        /**
         * Sets the given detail value.
         *
         * @param value the new value of the detail
         */
        @SuppressWarnings("unchecked")
        public void setDetail(String value)
        {
            //let's save in addressbook
            if(isMultiline(getCategory()))
            {
                // get others
                List<ContactDetail> details =
                    parent.getContactDetails(getCategory());

                // first add existing one
                ArrayList values = new ArrayList();
                for(ContactDetail cd : details)
                {
                    String det = cd.getDetail();

                    for(ContactDetail.SubCategory sub : cd.getSubCategories())
                    {
                        String label = getLabel(property, sub, subPropertyLabel);

                        if(label != null)
                        {
                            if(getSubCategories().contains(sub))
                                values.add(value);
                            else
                                values.add(det);

                            values.add(label);
                        }
                    }
                }

                // now the real edit
                setProperty(
                    id,
                    ABPERSON_PROPERTIES[property],
                    subPropertyLabel,
                    values.toArray(new Object[values.size()]));
            }
            else
            {
                setProperty(id, ABPERSON_PROPERTIES[property], null, value);
            }

            super.setDetail(value);
        }

        /**
         * Sets the id of the parent contact source.
         * @param id
         */
        public void setId(String id)
        {
            this.id = id;
        }

        /**
         * Sets the parent source contact.
         * @param parent the parent source contact
         */
        public void setParent(AddressBookSourceContact parent)
        {
            this.parent = parent;
        }

        /**
         * Returns the sub property.
         * @return
         */
        public String getSubPropertyLabel()
        {
            return subPropertyLabel;
        }
    }
}
